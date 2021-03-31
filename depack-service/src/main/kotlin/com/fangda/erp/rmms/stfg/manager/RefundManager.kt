package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.rmms.stfg.bo.RefundMachine
import com.fangda.erp.rmms.stfg.db.dao.RefundDao
import com.fangda.erp.rmms.stfg.db.dataobject.refund.RefundCheckPhotoDO
import com.fangda.erp.rmms.stfg.db.dataobject.refund.RefundMachineDO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

/**
 * @author yuhb
 * @date 2021/3/10
 */
@Service
class RefundManager @Autowired constructor(
    private val refundDao: RefundDao
) {
    /**
     * IO
     * 查询所有待退货车辆信息
     */
    fun listRefundMachines(): List<RefundMachine> {
        return this.refundDao.listRefundMachines().map(this::convertRefundMachineDoToBo)
    }

    @Transactional
    fun checkRefundMachine(
        rawMachineId: String,
        approved: Boolean,
        operator: String,
        checkReason: String,
        photos: List<File>
    ): Boolean {
        photos.forEachIndexed { index, photo ->
            val refundCheckPhotoDO = RefundCheckPhotoDO().apply {
                this.rawMachineId = rawMachineId
                this.no = index + 1
                this.photo = photo.readBytes()
            }
            if (refundCheckPhotoDO.photo.isNotEmpty()) {
                assertPersistSuccess(refundDao.insertRefundCheckPhotos(refundCheckPhotoDO) == 1) {
                    "插入退货照片失败"
                }
            }
        }
        val finalState = if (approved) {
            assertPersistSuccess(refundDao.updateMachinePlanState(rawMachineId, -10) == 1) {
                "更新退货状态失败"
            }
            0
        } else -1
        return assertPersistSuccess(
            refundDao.updateRefundRecord(
                rawMachineId,
                finalState,
                operator,
                checkReason
            ) == 1
        ) {
            "更新退货信息失败"
        }
    }

    fun getRefundCheckPhoto(rawMachineId: String, photoNo: Int): RefundCheckPhotoDO {
        return refundDao.getRefundCheckPhoto(rawMachineId, photoNo)
    }

    // ---- private ----
    private fun convertRefundMachineDoToBo(refundMachineDO: RefundMachineDO): RefundMachine {
        return RefundMachine().apply {
            this.rawMachineId = refundMachineDO.rawMachineId!!
            this.liscenceNo = refundMachineDO.liscenceNo!!
            this.flowNo = refundMachineDO.flowNo!!
            this.refundDate = refundMachineDO.refundDate!!
            this.refundOper = refundMachineDO.refundOper!!
            this.refundReason = refundMachineDO.refundReason ?: ""
        }
    }

}