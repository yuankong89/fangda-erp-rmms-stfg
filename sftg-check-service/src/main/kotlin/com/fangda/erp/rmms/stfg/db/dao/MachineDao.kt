package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.bo.accept.AcceptableMachine
import com.fangda.erp.rmms.stfg.bo.accept.AcceptedMachine
import com.fangda.erp.rmms.stfg.bo.accept.MidAcceptedMachine
import com.fangda.erp.rmms.stfg.bo.unload.UnloadedMachine
import com.fangda.erp.rmms.stfg.bo.unload.UnloadingMachine
import org.apache.ibatis.annotations.Mapper
import java.time.LocalDateTime

/**
 * 因为仅涉及到查询，因此都没有采用DO模型查询
 * @author yuhb
 * @date 2020/11/26
 */
@Mapper
interface MachineDao {
    // 数据查询
    fun listUnloadingMachinesByTypeAndLevel(
        typeCodeList: List<String>,
        levelCodeList: List<String>
    ): List<UnloadingMachine>

    fun listUnloadedMachineByUserId(userId: String): List<UnloadedMachine>
    fun listAcceptableMachinesByAndLevel(
        typeCodeList: List<String>,
        levelCodeList: List<String>
    ): List<AcceptableMachine>

    fun listAcceptedMachinesByUserId(userId: String): List<AcceptedMachine>
    fun listAllAcceptedMachines(typeCodeList: List<String>): List<AcceptedMachine>
    fun listMidAcceptedMachines(
        typeCodeList: List<String>,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<MidAcceptedMachine>

    // 流水号生成
    fun getMaxFlowNo(prefix: String): String
    fun insertMachineFlowNo(rawMachineId: String, newFlowNo: String): Int
}