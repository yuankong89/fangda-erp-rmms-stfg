package com.fangda.erp.rmms.stfg.service

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.libs.utils.TimeUtils
import com.fangda.erp.rmms.stfg.bo.accept.AcceptableMachine
import com.fangda.erp.rmms.stfg.bo.accept.AcceptedMachine
import com.fangda.erp.rmms.stfg.bo.accept.MidAcceptedMachine
import com.fangda.erp.rmms.stfg.bo.unload.UnloadedMachine
import com.fangda.erp.rmms.stfg.bo.unload.UnloadingMachine
import com.fangda.erp.rmms.stfg.dto.accept.AcceptableMachineDto
import com.fangda.erp.rmms.stfg.dto.accept.AcceptedMachineDto
import com.fangda.erp.rmms.stfg.dto.accept.MidAcceptedMachineDto
import com.fangda.erp.rmms.stfg.dto.unload.UnloadedMachineDto
import com.fangda.erp.rmms.stfg.dto.unload.UnloadingMachineDto
import com.fangda.erp.rmms.stfg.manager.MachineManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/11/24
 */
@Service
class MachineService @Autowired constructor(
    private val machineManager: MachineManager
) {
    /**
     * 通过[typeCodes]来获取可卸货的车辆信息
     * 每次调用时自动生成车辆的flowNo
     */
    fun listUnloadingMachines(typeCodes: String, levelCodes: String): Mono<List<UnloadingMachineDto>> {
        return Mono.just(Pair(typeCodes, levelCodes))
            .map { Pair(it.first.split(","), it.second.split(",")) }
            .map { machineManager.listUnloadingMachinesByTypeAndLevel(it.first, it.second) }
            .flatMapMany { Flux.fromIterable(it) }
            .doOnNext(machineManager::generateMachineFlowNo)
            .map(this::convertUnloadingMachineBOToDto)
            .collectList()
    }

    /**
     * 通过[typeCodes]来获取可验收的车辆信息
     * 每次调用时自动生成车辆的flowNo
     */
    fun listAcceptableMachines(typeCodes: String, levelCodes: String): Mono<List<AcceptableMachineDto>> {
        return Mono.just(Pair(typeCodes, levelCodes))
            .map { Pair(it.first.split(","), it.second.split(",")) }
            .map { machineManager.listAcceptableMachinesByTypeCodes(it.first, it.second) }
            .flatMapMany { Flux.fromIterable(it) }
            .doOnNext(machineManager::generateMachineFlowNo)
            .map(this::convertAcceptableMachineBOToDto)
            .collectList()
    }

    /**
     * 通过用户来获取可以检查的车辆卸车信息
     */
    fun listSelfCheckUnloadedMachines(fangdaPrincipal: FangdaPrincipal): Mono<List<UnloadedMachineDto>> {
        return Mono.fromCallable {
            machineManager.listUnloadedMachinesByUserId(fangdaPrincipal.userId).map(this::convertUnloadedMachineBOToDto)
        }
    }

    /**
     * 通过用户来获取可以检查的车辆信息
     */
    fun listSelfCheckAcceptedMachines(fangdaPrincipal: FangdaPrincipal): Mono<List<AcceptedMachineDto>> {
        return Mono.fromCallable {
            machineManager.listAcceptedMachinesByUserId(fangdaPrincipal.userId).map(this::convertAcceptedMachineBOToDto)
        }
    }

    /**
     * 获得全部验收完成的车辆信息
     */
    fun listAcceptedMachines(): Mono<List<AcceptedMachineDto>> {
        return Mono.fromCallable {
            machineManager.listAllAcceptedMachines().map(this::convertAcceptedMachineBOToDto)
        }
    }

    /**
     * 通过时间查询，获得中段验收完成的车辆信息
     */
    fun listMidAcceptedMachines(startTimeStr: String, endTimeStr: String): Mono<List<MidAcceptedMachineDto>> {
        return Mono.fromCallable {
            val startTime = TimeUtils.parse(startTimeStr, "yyyy-MM-dd HH:mm:ss")
            val endTime = TimeUtils.parse(endTimeStr, "yyyy-MM-dd HH:mm:ss")
            machineManager.listMidAcceptedMachines(startTime, endTime).map(this::convertMidAcceptedMachineBOToDto)
        }
    }

    // ---- private ----
    fun convertUnloadingMachineBOToDto(unloadingMachine: UnloadingMachine): UnloadingMachineDto {
        return UnloadingMachineDto().apply {
            this.rawMachineId = unloadingMachine.rawMachineId
            this.flowNo = unloadingMachine.flowNo
            this.liscenceNo = unloadingMachine.liscenceNo
            this.rawCode = unloadingMachine.rawCode
            this.levelCode = unloadingMachine.levelCode
            this.rawName = unloadingMachine.rawName

            this.daySort = unloadingMachine.daySort.toString()
            this.grossWeight = unloadingMachine.grossWeight.toString()
            this.grossWeightTime = TimeUtils.format(unloadingMachine.grossWeightTime, "yyyy-MM-dd HH:mm:ss")
            this.preRawLevel = unloadingMachine.preRawLevel
        }
    }

    fun convertUnloadedMachineBOToDto(unloadedMachine: UnloadedMachine): UnloadedMachineDto {
        return UnloadedMachineDto().apply {
            this.rawMachineId = unloadedMachine.rawMachineId
            this.flowNo = unloadedMachine.flowNo
            this.liscenceNo = unloadedMachine.liscenceNo
            this.rawCode = unloadedMachine.rawCode
            this.levelCode = unloadedMachine.levelCode
            this.rawName = unloadedMachine.rawName

            this.grossWeight = unloadedMachine.grossWeight.toString()
            this.grossWeightTime = TimeUtils.format(unloadedMachine.grossWeightTime, "yyyy-MM-dd HH:mm:ss")

            this.selfChecked = unloadedMachine.selfChecked
            this.unloadTime = TimeUtils.format(unloadedMachine.unloadTime, "yyyy-MM-dd HH:mm:ss")
        }
    }

    fun convertAcceptableMachineBOToDto(acceptableMachine: AcceptableMachine): AcceptableMachineDto {
        return AcceptableMachineDto().apply {
            this.rawMachineId = acceptableMachine.rawMachineId
            this.flowNo = acceptableMachine.flowNo
            this.liscenceNo = acceptableMachine.liscenceNo
            this.rawCode = acceptableMachine.rawCode
            this.levelCode = acceptableMachine.levelCode
            this.rawName = acceptableMachine.rawName

            this.daySort = acceptableMachine.daySort.toString()
            this.grossWeight = acceptableMachine.grossWeight.toString()
            this.grossWeightTime = TimeUtils.format(acceptableMachine.grossWeightTime, "yyyy-MM-dd HH:mm:ss")
            this.preRawLevel = acceptableMachine.preRawLevel
        }
    }

    fun convertAcceptedMachineBOToDto(acceptedMachine: AcceptedMachine): AcceptedMachineDto {
        return AcceptedMachineDto().apply {
            this.rawMachineId = acceptedMachine.rawMachineId
            this.flowNo = acceptedMachine.flowNo
            this.liscenceNo = acceptedMachine.liscenceNo
            this.rawCode = acceptedMachine.rawCode
            this.levelCode = acceptedMachine.levelCode
            this.rawName = acceptedMachine.rawName

            this.grossWeight = acceptedMachine.grossWeight.toString()
            this.grossWeightTime = TimeUtils.format(acceptedMachine.grossWeightTime, "yyyy-MM-dd HH:mm:ss")
        }
    }

    fun convertMidAcceptedMachineBOToDto(midAcceptedMachine: MidAcceptedMachine): MidAcceptedMachineDto {
        return MidAcceptedMachineDto().apply {
            this.rawMachineId = midAcceptedMachine.rawMachineId
            this.flowNo = midAcceptedMachine.flowNo
            this.liscenceNo = midAcceptedMachine.liscenceNo
            this.rawCode = midAcceptedMachine.rawCode
            this.levelCode = midAcceptedMachine.levelCode
            this.rawName = midAcceptedMachine.rawName

            this.submitTime = TimeUtils.format(midAcceptedMachine.submitTime, "yyyy-MM-dd HH:mm:ss")
            this.acceptTime = TimeUtils.format(midAcceptedMachine.acceptTime, "yyyy-MM-dd HH:mm:ss")
            this.acceptOperator = midAcceptedMachine.acceptOperator
        }
    }
}