package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.libs.utils.TimeUtils
import com.fangda.erp.rmms.stfg.bo.depack.DePackData
import com.fangda.erp.rmms.stfg.bo.depack.DePackDataDetail
import com.fangda.erp.rmms.stfg.bo.depack.DePackMachine
import com.fangda.erp.rmms.stfg.db.dao.DePackDao
import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackDataDO
import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackDetailDO
import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackMachineDO
import com.fangda.erp.rmms.stfg.db.dataobject.depack.DePackPhotoDO
import com.fangda.erp.rmms.stfg.db.query.DePackMachineQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/10
 */
@Service
@Transactional
class DePackManager @Autowired constructor(
    private val dePackDao: DePackDao
) {
    /**
     * IO
     * 通过条件查询拆包车辆信息
     */
    fun listDePackMachineByCriteria(
        stateIn: String? = null,
        supplierId: String? = null,
        startTime: LocalDateTime? = null,
        endTime: LocalDateTime? = null
    ): List<DePackMachine> {
        return dePackDao.listDePackMachineByCriteria(DePackMachineQuery().apply {
            this.stateIn = stateIn?.split(",")
            this.supplierId = supplierId
            this.startTime = startTime
            this.endTime = endTime
        }).map {
            this.convertDePackMachineDOToBO(it)
        }
    }

    /**
     * IO
     * 通过车辆ID获取验收数据
     */
    fun getDePackData(rawMachineId: String): DePackData {
        val detailDOs = dePackDao.listDePackDetailById(rawMachineId)
        val dePackPhotoCount = dePackDao.countDePackPhotoById(rawMachineId)
        return DePackData().apply {
            this.rawMachineId = rawMachineId
            this.dePackPhotoCount = dePackPhotoCount
            this.details = detailDOs.map { convertDePackDetailDOToBO(it) }
        }
    }

    /**
     * IO
     * 车辆需要解包验收
     */
    @Throws(PersistObjectException::class)
    fun needDePack(rawMachineId: String, operator: String): Boolean {
        val dePackDataDO = DePackDataDO().apply {
            this.rawMachineId = rawMachineId
            this.state = DePackMachine.DePackMachineEnum.NEED.value
            this.creator = operator
        }
        return assertPersistSuccess(dePackDao.insertDePackData(dePackDataDO) == 1) {
            "插入数据失败!"
        }
    }

    /**
     * IO
     * 车辆开始解包
     */
    @Throws(PersistObjectException::class)
    fun startDePack(rawMachineId: String, operator: String): Boolean {
        val dePackDataDO = DePackDataDO().apply {
            this.rawMachineId = rawMachineId
            this.state = DePackMachine.DePackMachineEnum.START.value
            this.startTime = TimeUtils.now()
            this.startOperator = operator
        }
        return assertPersistSuccess(dePackDao.updateDePackData(dePackDataDO) == 1) {
            "更新数据失败!"
        }
    }

    /**
     * IO
     * 车辆完成解包
     */
    @Throws(PersistObjectException::class)
    fun endDePack(
        rawMachineId: String,
        details: List<DePackDataDetail>,
        photos: List<File>,
        operator: String
    ): Boolean {
        photos.forEachIndexed { index, file ->
            val dePackPhotoDO = DePackPhotoDO().apply {
                this.rawMachineId = rawMachineId
                this.no = index + 1
                this.photo = file.readBytes()
            }
            assertPersistSuccess(dePackDao.insertDePackPhoto(dePackPhotoDO) == 1) {
                "插入解包照片数据失败"
            }
        }
        details.forEach {
            val dePackDetailDO = DePackDetailDO().apply {
                this.rawMachineId = rawMachineId
                this.dataName = it.dataName
                this.dataNo = it.dataNo
                this.dataValue = it.dataValue
            }
            assertPersistSuccess(dePackDao.insertDePackDataDetail(dePackDetailDO) == 1) {
                "插入解包明细数据失败"
            }
        }
        val dePackDataDO = DePackDataDO().apply {
            this.rawMachineId = rawMachineId
            this.state = DePackMachine.DePackMachineEnum.END.value
            this.endTime = TimeUtils.now()
            this.checkOperator = operator
        }
        return assertPersistSuccess(dePackDao.updateDePackData(dePackDataDO) == 1) {
            "更新数据失败!"
        }
    }

    /**
     * IO
     * 修改解包数据
     */
    @Throws(PersistObjectException::class)
    fun modifyDePackData(
        rawMachineId: String,
        details: List<DePackDataDetail>,
        photos: List<File>,
        operator: String
    ): Boolean {
        // 删除数据
        this.dePackDao.deleteDePackDataDetail(rawMachineId)
        this.dePackDao.deleteDePackPhoto(rawMachineId)
        return this.endDePack(rawMachineId, details, photos, operator)
    }

    /**
     * IO
     * 车辆解包数据审核结束
     */
    @Throws(PersistObjectException::class)
    fun passDePack(rawMachineId: String, operator: String): Boolean {
        val dePackDataDO = DePackDataDO().apply {
            this.rawMachineId = rawMachineId
            this.state = DePackMachine.DePackMachineEnum.PASS.value
            this.passTime = TimeUtils.now()
            this.passOperator = operator
        }
        return assertPersistSuccess(dePackDao.updateDePackData(dePackDataDO) == 1) {
            "更新数据失败!"
        }
    }

    // ---- private ----
    private fun convertDePackMachineDOToBO(machine: DePackMachineDO): DePackMachine {
        return DePackMachine().apply {
            this.rawMachineId = machine.rawMachineId
            this.flowNo = machine.flowNo
            this.liscenceNo = machine.liscenceNo
            this.state = DePackMachine.DePackMachineEnum.valueOf(machine.state)
        }
    }

    private fun convertDePackDetailDOToBO(detail: DePackDetailDO): DePackDataDetail {
        return DePackDataDetail().apply {
            this.dataNo = detail.dataNo
            this.dataName = detail.dataName
            this.dataValue = detail.dataValue
        }
    }
}