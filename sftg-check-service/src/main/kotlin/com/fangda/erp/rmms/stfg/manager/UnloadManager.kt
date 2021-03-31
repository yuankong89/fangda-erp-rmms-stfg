package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.rmms.stfg.bo.Photo
import com.fangda.erp.rmms.stfg.bo.unload.UnloadData
import com.fangda.erp.rmms.stfg.db.dao.UnloadDao
import com.fangda.erp.rmms.stfg.db.dataobject.unload.UnloadDataDO
import com.fangda.erp.rmms.stfg.db.dataobject.unload.UnloadPhotoDO
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
class UnloadManager @Autowired constructor(
    private val unloadDao: UnloadDao
) {
    /**
     * 处理卸车验收数据
     */
    @Throws(PersistObjectException::class)
    fun newUnloadData(unloadData: UnloadData, photos: List<File>, operator: String): Boolean {
        // 插入主记录
        val unloadDataDO = UnloadDataDO().apply {
            this.rawMachineId = unloadData.rawMachineId
            this.unloadPos = unloadData.unloadPos
            this.checkTime = unloadData.unloadDate
            this.remark = unloadData.unloadRemark
            this.operator = operator
        }
        assertPersistSuccess(unloadDao.insertUnloadData(unloadDataDO) == 1) {
            "插入卸货主记录错误"
        }
        photos.forEachIndexed { index, file ->
            val unloadPhotoDO = UnloadPhotoDO().apply {
                this.rawMachineId = rawMachineId
                this.no = index + 1
                this.photo = file.readBytes()
            }
            assertPersistSuccess(unloadDao.insertUnloadPhoto(unloadPhotoDO) == 1) {
                "插入卸货验收照片数据失败"
            }
        }
        return true
    }

    /**
     * IO
     * 自我检查
     */
    @Throws(PersistObjectException::class)
    fun selfCheckUnload(rawMachineId: String): Boolean {
        if (unloadDao.countSelfCheckFlag(rawMachineId) == 0) {
            unloadDao.insertSelfCheckFlag(rawMachineId)
        }
        return true
    }

    /**
     * IO
     * 修改卸车验收数据
     */
    @Throws(PersistObjectException::class)
    fun modifyUnloadData(unloadData: UnloadData, photos: List<File>, operator: String): Boolean {
        // 修改主记录
        val unloadDataDO = UnloadDataDO().apply {
            this.rawMachineId = unloadData.rawMachineId
            this.unloadPos = unloadData.unloadPos
            this.checkTime = unloadData.unloadDate
            this.remark = unloadData.unloadRemark
            this.operator = operator
        }
        assertPersistSuccess(unloadDao.updateUnloadData(unloadDataDO) == 1) {
            "更新卸货主记录错误"
        }
        // 删除照片
        unloadDao.deleteUnloadPhotos(unloadDataDO.rawMachineId)
        photos.forEachIndexed { index, file ->
            val unloadPhotoDO = UnloadPhotoDO().apply {
                this.rawMachineId = rawMachineId
                this.no = index + 1
                this.photo = file.readBytes()
            }
            assertPersistSuccess(unloadDao.insertUnloadPhoto(unloadPhotoDO) == 1) {
                "插入卸货验收照片数据失败"
            }
        }
        return true
    }

    fun getUnloadData(rawMachineId: String): UnloadData {
        val unloadData = unloadDao.getUnloadData(rawMachineId)
        return UnloadData().apply {
            this.unloadPos = unloadData.unloadPos
            this.unloadDate = unloadData.checkTime
            this.unloadRemark = unloadData.remark ?: ""
        }
    }

    fun getUnloadPhoto(rawMachineId: String, photoNo: Int): Photo {
        val unloadPhotoDO = unloadDao.getUnloadPhoto(rawMachineId, photoNo)
        return Photo().apply {
            this.data = unloadPhotoDO.photo
        }
    }
}