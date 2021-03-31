package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.rmms.stfg.bo.Machine
import com.fangda.erp.rmms.stfg.bo.accept.AcceptableMachine
import com.fangda.erp.rmms.stfg.bo.accept.AcceptedMachine
import com.fangda.erp.rmms.stfg.bo.accept.MidAcceptedMachine
import com.fangda.erp.rmms.stfg.bo.unload.UnloadedMachine
import com.fangda.erp.rmms.stfg.bo.unload.UnloadingMachine
import com.fangda.erp.rmms.stfg.db.dao.MachineDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/25
 */
@Service
class MachineManager @Autowired constructor(
    private val machineDao: MachineDao
) {
    /**
     * 根据物资编码获取所有待卸货的车辆信息
     */
    fun listUnloadingMachinesByTypeAndLevel(
        typeCodeList: List<String>,
        levelCodeList: List<String>
    ): List<UnloadingMachine> {
        return machineDao.listUnloadingMachinesByTypeAndLevel(typeCodeList, levelCodeList)
    }

    /**
     * 根据[userId]获取卸货验收完成的车辆信息
     */
    fun listUnloadedMachinesByUserId(userId: String): List<UnloadedMachine> {
        return machineDao.listUnloadedMachineByUserId(userId)
    }

    /**
     * 根据物资编码获取所有的待验收的车辆信息
     */
    fun listAcceptableMachinesByTypeCodes(
        typeCodeList: List<String>,
        levelCodeList: List<String>
    ): List<AcceptableMachine> {
        return machineDao.listAcceptableMachinesByAndLevel(typeCodeList, levelCodeList)
    }

    /**
     * 根据[userId]获取验收完成的车辆信息
     */
    fun listAcceptedMachinesByUserId(userId: String): List<AcceptedMachine> {
        return machineDao.listAcceptedMachinesByUserId(userId)
    }

    /**
     * 获得全部验收完成的车辆
     */
    fun listAllAcceptedMachines(): List<AcceptedMachine> {
        return machineDao.listAllAcceptedMachines(DefinitionManager.getAllTypes().split(","))
    }

    /**
     * 通过时间获得中段验收完成的车辆
     */
    fun listMidAcceptedMachines(startTime: LocalDateTime, endTime: LocalDateTime): List<MidAcceptedMachine> {
        return machineDao.listMidAcceptedMachines(DefinitionManager.getAllTypes().split(","), startTime, endTime)
    }

    /**
     * IO
     * 生产车辆在检化验中的流水号
     */
    @Transactional
    @Throws(PersistObjectException::class)
    fun generateMachineFlowNo(machine: Machine) {
        if (machine.flowNo.isEmpty()) {
            val todayYearStr = LocalDate.now().year.toString().substring(2)
            val prefix = when {
                isYk(machine.rawCode) -> {
                    "YK${todayYearStr}-"
                }
                isFg(machine.rawCode) -> {
                    "FG${todayYearStr}-"
                }
                isSt(machine.rawCode) -> {
                    "ST${todayYearStr}-"
                }
                else -> {
                    return
                }
            }
            val maxFlowNo = machineDao.getMaxFlowNo(prefix)
            val newNo = if (maxFlowNo.isEmpty()) {
                1
            } else {
                maxFlowNo.substring(5).toInt() + 1
            }
            val newFlowNo = prefix + String.format("%05d", newNo)
            machineDao.insertMachineFlowNo(machine.rawMachineId, newFlowNo)
            machine.flowNo = newFlowNo
        }
    }

    // ---- private ----
    private fun isYk(typeCode: String): Boolean {
        return DefinitionManager.YK_TYPE_DEF.defData.contains(typeCode)
    }

    private fun isFg(typeCode: String): Boolean {
        return DefinitionManager.FG_TYPE_DEF.defData.contains(typeCode)
    }

    private fun isSt(typeCode: String): Boolean {
        return DefinitionManager.ST_TYPE_DEF.defData.contains(typeCode)
    }
}