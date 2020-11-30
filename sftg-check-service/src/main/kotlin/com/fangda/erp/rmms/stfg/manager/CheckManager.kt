package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.rmms.stfg.db.dao.AcceptDao
import com.fangda.erp.rmms.stfg.db.dao.CheckDao
import com.fangda.erp.rmms.stfg.db.dao.UnloadDao
import com.fangda.erp.rmms.stfg.db.dataobject.accept.AcceptCheckRecordDO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Service
class CheckManager @Autowired constructor(
    private val acceptDao: AcceptDao,
    private val unloadDao: UnloadDao,
    private val checkDao: CheckDao
) {
    /**
     * IO
     * 审核通过
     */
    @Throws(PersistObjectException::class)
    fun acceptApprove(rawMachineId: String, comment: String, operator: String): Boolean {
        assertPersistSuccess(acceptDao.updateAcceptDataState(rawMachineId, 0) == 1) {
            "审核时更新状态出错"
        }
        val acceptCheckRecordDO = AcceptCheckRecordDO().apply {
            this.rawMachineId = rawMachineId
            this.origState = 1
            this.closeState = 0
            this.comment = comment
            this.checkOperator = operator
        }
        checkDao.insertAcceptCheckRecord(acceptCheckRecordDO)
        return true
    }

    /**
     * IO
     * 审核不通过
     */
    @Throws(PersistObjectException::class)
    fun acceptDeny(rawMachineId: String, comment: String, operator: String): Boolean {
        // 保存记录
        checkDao.backUpAcceptData(rawMachineId)
        checkDao.backUpAcceptDataDetail(rawMachineId)
        val acceptCheckRecordDO = AcceptCheckRecordDO().apply {
            this.rawMachineId = rawMachineId
            this.origState = 1
            this.closeState = -1
            this.comment = comment
            this.checkOperator = operator
        }
        checkDao.insertAcceptCheckRecord(acceptCheckRecordDO)
        // 删除数据
        acceptDao.deleteAcceptDataDetails(rawMachineId)
        acceptDao.deleteAcceptData(rawMachineId)
        acceptDao.deleteAcceptPhotos(rawMachineId)
        unloadDao.deleteUnloadData(rawMachineId)
        unloadDao.deleteUnloadPhotos(rawMachineId)
        // 删除自我检查Flag
        acceptDao.deleteSelfCheckFlag(rawMachineId)
        unloadDao.deleteSelfCheckFlag(rawMachineId)
        return true
    }
}