package com.fangda.erp.rmms.stfg.service

import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.libs.utils.JsonUtils
import com.fangda.erp.libs.utils.TimeUtils
import com.fangda.erp.rmms.stfg.bo.DePackData
import com.fangda.erp.rmms.stfg.bo.DePackDataDetail
import com.fangda.erp.rmms.stfg.bo.DePackMachine
import com.fangda.erp.rmms.stfg.dto.DePackDataDetailDto
import com.fangda.erp.rmms.stfg.dto.DePackDataDto
import com.fangda.erp.rmms.stfg.dto.DePackMachineDto
import com.fangda.erp.rmms.stfg.manager.DePackManager
import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/10
 */
@Service
class DePackService @Autowired constructor(
    private val dePackManager: DePackManager
) {
    fun needDePack(rawMachineId: String, operator: String): Mono<Boolean> {
        return Mono.fromCallable { dePackManager.needDePack(rawMachineId, operator) }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    fun startDePack(rawMachineId: String, operator: String): Mono<Boolean> {
        return Mono.fromCallable { dePackManager.startDePack(rawMachineId, operator) }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    fun endDePack(
        rawMachineId: String,
        dePackData: String,
        photos: List<File>,
        operator: String
    ): Mono<Boolean> {
        return Mono.fromCallable {
            JsonUtils.parseObject(
                dePackData,
                object : TypeReference<List<DePackDataDetailDto>>() {})
        }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .flatMapMany { Flux.fromIterable(it!!) }
            .map { convertDePackDataDetailDtoToBO(it) }
            .collectList()
            .flatMap { Mono.fromCallable { dePackManager.endDePack(rawMachineId, it, photos, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    fun listDePackMachinesByCriteria(
        stateIn: String? = null,
        supplierId: String? = null,
        startTimeStr: String? = null,
        endTimeStr: String? = null
    ): Mono<List<DePackMachineDto>> {
        var startTime: LocalDateTime? = null
        var endTime: LocalDateTime? = null
        if (startTimeStr != null && endTimeStr != null) {
            startTime = TimeUtils.parse(startTimeStr, "yyyy-MM-dd HH:mm:ss")
            endTime = TimeUtils.parse(endTimeStr, "yyyy-MM-dd HH:mm:ss")
        }
        return Mono.fromCallable { dePackManager.listDePackMachineByCriteria(stateIn, supplierId, startTime, endTime) }
            .flatMapMany { Flux.fromIterable(it) }
            .map { this.convertDePackMachineBOToDto(it) }
            .collectList()
    }

    fun getDePackData(rawMachineId: String): Mono<DePackDataDto> {
        return Mono.fromCallable { dePackManager.getDePackData(rawMachineId) }
            .map { this.convertDePackDataBOToDto(it) }
    }

    fun modifyDePackData(
        rawMachineId: String,
        dePackData: String,
        photos: List<File>,
        operator: String
    ): Mono<Boolean> {
        return Mono.fromCallable {
            JsonUtils.parseObject(
                dePackData,
                object : TypeReference<List<DePackDataDetailDto>>() {})
        }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .flatMapMany { Flux.fromIterable(it!!) }
            .map { convertDePackDataDetailDtoToBO(it) }
            .collectList()
            .flatMap { Mono.fromCallable { dePackManager.modifyDePackData(rawMachineId, it, photos, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    fun passDePack(rawMachineId: String, operator: String): Mono<Boolean> {
        return Mono.fromCallable { dePackManager.passDePack(rawMachineId, operator) }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    fun listDePackTraceMachines(): Mono<List<DePackMachineDto>> {
        return Mono.fromCallable { dePackManager.listDePackTraceMachine() }
            .flatMapMany { Flux.fromIterable(it) }
            .map { this.convertDePackMachineBOToDto(it) }
            .collectList()
    }

    fun endDePackTrace(rawMachineId: String, dePackData: String, operator: String): Mono<Boolean> {
        return Mono.fromCallable {
            JsonUtils.parseObject(
                dePackData,
                object : TypeReference<List<DePackDataDetailDto>>() {})
        }
            .switchIfEmpty(Mono.error(BusinessException("解析数据错误!")))
            .flatMapMany { Flux.fromIterable(it!!) }
            .map { convertDePackDataDetailDtoToBO(it) }
            .collectList()
            .flatMap { Mono.fromCallable { dePackManager.endDePackTrace(rawMachineId, it, operator) } }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    // ---- private -----
    private fun convertDePackDataDetailDtoToBO(detail: DePackDataDetailDto): DePackDataDetail {
        return DePackDataDetail().apply {
            this.dataNo = detail.dataNo
            this.dataName = detail.dataName
            this.dataValue = detail.amount.toDouble()
        }
    }

    private fun convertDePackMachineBOToDto(machine: DePackMachine): DePackMachineDto {
        return DePackMachineDto().apply {
            this.rawMachineId = machine.rawMachineId
            this.flowNo = machine.flowNo
            this.liscenceNo = machine.liscenceNo
            this.state = machine.state.value.toString()
            this.stateStr = machine.state.stateStr
            if (machine.createTime != null) {
                this.createTime = TimeUtils.format(machine.createTime!!, "yyyy-MM-dd HH:mm:ss")
            } else {
                this.createTime = ""
            }
        }
    }

    private fun convertDePackDataBOToDto(data: DePackData): DePackDataDto {
        return DePackDataDto().apply {
            this.rawMachineId = data.rawMachineId
            this.dePackPhotoCount = data.dePackPhotoCount
            this.details = data.details.map { convertDePackDataDetailBOToDto(it) }
        }
    }

    private fun convertDePackDataDetailBOToDto(detail: DePackDataDetail): DePackDataDetailDto {
        return DePackDataDetailDto().apply {
            this.dataNo = detail.dataNo
            this.dataName = detail.dataName
            this.amount = detail.dataValue.toString()
        }
    }
}