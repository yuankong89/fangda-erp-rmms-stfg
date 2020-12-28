package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.rmms.stfg.bo.Photo
import com.fangda.erp.rmms.stfg.bo.accept.AcceptData
import com.fangda.erp.rmms.stfg.bo.accept.AcceptDataDetail
import com.fangda.erp.rmms.stfg.db.dao.AcceptDao
import com.fangda.erp.rmms.stfg.db.dao.DePackDao
import com.fangda.erp.rmms.stfg.db.dao.MidAcceptDao
import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptDataDO
import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptDetailDO
import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptPhotoDO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Service
@Transactional
class AcceptManager @Autowired constructor(
    private val acceptDao: AcceptDao,
    private val midAcceptDao: MidAcceptDao,
    private val dePackDao: DePackDao
) {
    /**
     * IO
     * 新增中段验收数据
     */
    @Throws(PersistObjectException::class)
    fun newMidAcceptData(
        acceptData: AcceptData,
        operator: String
    ): Boolean {
        val acceptDataDO = AcceptDataDO().apply {
            this.rawMachineId = acceptData.rawMachineId
            this.acceptPos = acceptData.acceptPos
            this.checkTime = acceptData.acceptDate
            this.remark = acceptData.acceptRemark
            this.operator = operator
        }
        assertPersistSuccess(midAcceptDao.insertMidAcceptData(acceptDataDO) == 1) {
            "插入中段验收主记录错误"
        }
        acceptData.details.forEach {
            val acceptDetailDO = AcceptDetailDO().apply {
                this.rawMachineId = rawMachineId
                this.dataName = it.dataName
                this.dataNo = it.dataNo
                this.dataValue = it.dataValue
            }
            assertPersistSuccess(midAcceptDao.insertMidAcceptDataDetail(acceptDetailDO) == 1) {
                "插入中段验收明细数据失败"
            }
        }
        return true
    }

    /**
     * IO
     * 新增验收数据
     */
    @Throws(PersistObjectException::class)
    fun newAcceptData(
        acceptData: AcceptData,
        photos: List<File>,
        operator: String
    ): Boolean {
        // 插入主记录
        val acceptDataDO = AcceptDataDO().apply {
            this.rawMachineId = acceptData.rawMachineId
            this.acceptPos = acceptData.acceptPos
            this.checkTime = acceptData.acceptDate
            this.remark = acceptData.acceptRemark
            this.operator = operator
        }
        assertPersistSuccess(acceptDao.insertAcceptData(acceptDataDO) == 1) {
            "插入验收主记录错误"
        }
        photos.forEachIndexed { index, file ->
            val acceptPhotoDO = AcceptPhotoDO().apply {
                this.rawMachineId = rawMachineId
                this.no = index + 1
                this.photo = file.readBytes()
            }
            assertPersistSuccess(acceptDao.insertAcceptPhoto(acceptPhotoDO) == 1) {
                "插入验收验收照片数据失败"
            }
        }
        acceptData.details.forEach {
            val acceptDetailDO = AcceptDetailDO().apply {
                this.rawMachineId = rawMachineId
                this.dataName = it.dataName
                this.dataNo = it.dataNo
                this.dataValue = it.dataValue
            }
            assertPersistSuccess(acceptDao.insertAcceptDataDetail(acceptDetailDO) == 1) {
                "插入验收明细数据失败"
            }
        }
        return true
    }

    /**
     * IO
     * 自我检查
     */
    @Throws(PersistObjectException::class)
    fun selfCheckAccept(rawMachineId: String): Boolean {
        if (acceptDao.countSelfCheckFlag(rawMachineId) == 0) {
            acceptDao.insertSelfCheckFlag(rawMachineId)
        }
        return true
    }

    /**
     * IO
     * 修改验收数据
     */
    @Throws(PersistObjectException::class)
    fun modifyAcceptData(
        acceptData: AcceptData,
        photos: List<File>,
        operator: String
    ): Boolean {
        // 修改主记录
        val acceptDataDO = AcceptDataDO().apply {
            this.rawMachineId = acceptData.rawMachineId
            this.acceptPos = acceptData.acceptPos
            this.checkTime = acceptData.acceptDate
            this.remark = acceptData.acceptRemark
            this.operator = operator
        }
        assertPersistSuccess(acceptDao.updateAcceptData(acceptDataDO) == 1) {
            "插入验收主记录错误"
        }
        // 删除照片
        acceptDao.deleteAcceptPhotos(acceptDataDO.rawMachineId)
        photos.forEachIndexed { index, file ->
            val acceptPhotoDO = AcceptPhotoDO().apply {
                this.rawMachineId = rawMachineId
                this.no = index + 1
                this.photo = file.readBytes()
            }
            assertPersistSuccess(acceptDao.insertAcceptPhoto(acceptPhotoDO) == 1) {
                "插入验收验收照片数据失败"
            }
        }
        // 删除明细数据
        acceptDao.deleteAcceptDataDetails(acceptDataDO.rawMachineId)
        acceptData.details.forEach {
            val acceptDetailDO = AcceptDetailDO().apply {
                this.rawMachineId = rawMachineId
                this.dataName = it.dataName
                this.dataNo = it.dataNo
                this.dataValue = it.dataValue
            }
            assertPersistSuccess(acceptDao.insertAcceptDataDetail(acceptDetailDO) == 1) {
                "插入验收明细数据失败"
            }
        }
        return true
    }

    fun getAcceptData(rawMachineId: String): AcceptData {
        val acceptData = acceptDao.selectAcceptData(rawMachineId)
        val acceptDataDetailDOs = acceptDao.listAcceptDataDetails(rawMachineId)
        val dePackPhotoCount = dePackDao.countDePackPhotoById(rawMachineId)
        val acceptPhotoCount = acceptDao.countAcceptPhotoById(rawMachineId)
        return AcceptData().apply {
            this.rawMachineId = rawMachineId
            this.acceptPos = acceptData.acceptPos
            this.acceptDate = acceptData.checkTime
            this.acceptRemark = acceptData.remark ?: ""
            this.dePackPhotoCount = dePackPhotoCount
            this.acceptPhotoCount = acceptPhotoCount
            this.details = acceptDataDetailDOs.map {
                AcceptDataDetail().apply {
                    this.dataName = it.dataName
                    this.dataNo = it.dataNo
                    this.dataValue = it.dataValue
                }
            }
        }
    }

    fun getMidAcceptData(rawMachineId: String): AcceptData {
        val acceptData = midAcceptDao.selectMidAcceptData(rawMachineId)
        val acceptDataDetailDOs = midAcceptDao.listMidAcceptDataDetails(rawMachineId)
        return AcceptData().apply {
            this.rawMachineId = rawMachineId
            this.acceptPos = acceptData.acceptPos
            this.acceptDate = acceptData.checkTime
            this.acceptRemark = acceptData.remark ?: ""
            this.details = acceptDataDetailDOs.map {
                AcceptDataDetail().apply {
                    this.dataName = it.dataName
                    this.dataNo = it.dataNo
                    this.dataValue = it.dataValue
                }
            }
        }
    }

    fun getAcceptPhoto(rawMachineId: String, photoNo: Int): Photo {
        val acceptPhotoDO = acceptDao.selectAcceptPhoto(rawMachineId, photoNo)
        return Photo().apply {
            this.data = acceptPhotoDO.photo
        }
    }
}