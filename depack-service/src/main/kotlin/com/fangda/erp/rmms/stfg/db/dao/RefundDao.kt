package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.refund.RefundCheckPhotoDO
import com.fangda.erp.rmms.stfg.db.dataobject.refund.RefundMachineDO

/**
 * @author yuhb
 * @date 2021/3/10
 */
interface RefundDao {
    fun listRefundMachines(): List<RefundMachineDO>
    fun getRefundCheckPhoto(rawMachineId: String, photoNo: Int): RefundCheckPhotoDO

    fun updateRefundRecord(rawMachineId: String, state: Int, operator: String, checkReason: String): Int

    fun insertRefundCheckPhotos(refundCheckPhotoDO: RefundCheckPhotoDO): Int
    fun updateMachinePlanState(rawMachineId: String, state: Int): Int
}