package com.fangda.erp.rmms.stfg.service

import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.rmms.stfg.bo.RefundMachine
import com.fangda.erp.rmms.stfg.dto.RefundMachineDto
import com.fangda.erp.rmms.stfg.manager.RefundManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.netty.ByteBufMono
import java.io.File
import java.time.format.DateTimeFormatter

/**
 * @author yuhb
 * @date 2021/3/10
 */
@Service
class RefundService @Autowired constructor(
    private val refundManager: RefundManager
) {
    fun listAllRefundMachines(): Mono<List<RefundMachineDto>> {
        return Mono.fromCallable { refundManager.listRefundMachines() }
            .flatMapMany { Flux.fromIterable(it) }
            .map(this::convertRefundMachineToDto)
            .collectList()
    }

    fun checkRefundMachine(
        rawMachineId: String,
        approved: String,
        operator: String,
        checkReason: String,
        photos: List<File>
    ): Mono<Boolean> {
        return Mono.fromCallable {
            refundManager.checkRefundMachine(
                rawMachineId,
                approved == "true",
                operator,
                checkReason,
                photos
            )
        }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    fun getRefundCheckPhoto(rawMachineId: String, photoNo: Int): Mono<ByteArray> {
        return Mono.fromCallable { refundManager.getRefundCheckPhoto(rawMachineId, photoNo) }
            .flatMap { ByteBufMono.just(it.photo) }
    }

    private fun convertRefundMachineToDto(refundMachine: RefundMachine): RefundMachineDto {
        return RefundMachineDto().apply {
            this.rawMachineId = refundMachine.rawMachineId
            this.liscenceNo = refundMachine.liscenceNo
            this.flowNo = refundMachine.flowNo
            this.refundDate = refundMachine.refundDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: ""
            this.refundOper = refundMachine.refundOper
            this.refundReason = refundMachine.refundReason
        }
    }
}