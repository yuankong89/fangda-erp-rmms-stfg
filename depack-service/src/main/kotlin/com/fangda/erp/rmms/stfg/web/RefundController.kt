package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.RefundMachineDto
import com.fangda.erp.rmms.stfg.service.RefundService
import com.fangda.erp.rmms.stfg.web.param.RefundParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Files

/**
 * @author yuhb
 * @date 2021/3/10
 */
@RestController
@RequestMapping("/refund")
class RefundController @Autowired constructor(
    private val refundService: RefundService
) {

    /**
     * 查询所有要退货的车辆
     */
    @GetMapping("")
    fun listRefundMachines(): Mono<List<RefundMachineDto>> {
        return refundService.listAllRefundMachines()
    }

    /**
     * 结束解包，提交解包数据
     * 上传解包照片
     * 以Json格式传递
     */
    @PostMapping("/{rawMachineId}/check")
    fun refundCheck(
        @PathVariable("rawMachineId") rawMachineId: String,
        @ModelAttribute refundParams: RefundParams,
        @RequestPart("photos") photos: Flux<FilePart>
    ): Mono<InvokeResultDto> {
        if (refundParams.approved == null) {
            Mono.just(InvokeResultDto.failResult("请输入同意或不同意"))
        }
        return photos.map {
            val tempFile = Files.createTempFile("tmp", it.filename())
            it.transferTo(tempFile)
            tempFile
        }
            .map { it.toFile() }
            .collectList()
            .flatMap {
                refundService.checkRefundMachine(
                    rawMachineId,
                    refundParams.approved!!,
                    refundParams.operator,
                    refundParams.checkReason,
                    it
                )
            }
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    @GetMapping("/{rawMachineId}/photos/{photoNo}")
    fun refundPhoto(
        @PathVariable("rawMachineId") rawMachineId: String,
        @PathVariable("photoNo") photoNo: Int,
        exchange: ServerWebExchange
    ): Mono<Void> {
        return Mono.just(exchange.response)
            .doOnNext { it.headers.contentType = MediaType.IMAGE_PNG }
            .zipWith(refundService.getRefundCheckPhoto(rawMachineId, photoNo))
            .flatMap { it.t1.writeAndFlushWith(Mono.just(Mono.just(it.t1.bufferFactory().wrap(it.t2)))) }
            .then()
    }
}