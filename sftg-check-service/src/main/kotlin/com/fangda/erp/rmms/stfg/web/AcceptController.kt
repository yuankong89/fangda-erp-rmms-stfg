package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.accept.AcceptDataDto
import com.fangda.erp.rmms.stfg.dto.accept.AcceptableMachineDto
import com.fangda.erp.rmms.stfg.dto.accept.AcceptedMachineDto
import com.fangda.erp.rmms.stfg.service.AcceptService
import com.fangda.erp.rmms.stfg.service.MachineService
import com.fangda.erp.rmms.stfg.utils.FileUtils
import com.fangda.erp.rmms.stfg.web.param.accept.AcceptDataParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/11/23
 */
@RestController
@RequestMapping("/accept")
@CrossOrigin
class AcceptController @Autowired constructor(
    private val machineService: MachineService,
    private val acceptService: AcceptService
) {
    /**
     * @param typeCodes 物资编码 如:"1000123, 1000321"
     * @param levelCodes 物资等级编码
     * 获取所有等待验收的车辆
     */
    @GetMapping("/machines")
    fun listAcceptableMachines(typeCodes: String, levelCodes: String): Mono<List<AcceptableMachineDto>> {
        return machineService.listAcceptableMachines(typeCodes, levelCodes)
    }

    /**
     * 获得可以检查的车辆
     */
    @GetMapping("/check")
    fun listSelfCheckMachines(auth: Authentication): Mono<List<AcceptedMachineDto>> {
        return machineService.listSelfCheckAcceptedMachines(auth.principal as FangdaPrincipal)
    }

    /**
     * 获得中段验收数据
     */
    @GetMapping("/mid/{rawMachineId}")
    fun getMidAcceptData(@PathVariable("rawMachineId") rawMachineId: String): Mono<AcceptDataDto> {
        return acceptService.getMidAcceptData(rawMachineId)
    }

    /**
     * 获得车辆验收数据
     */
    @GetMapping("/{rawMachineId}")
    fun getAcceptData(@PathVariable("rawMachineId") rawMachineId: String): Mono<AcceptDataDto> {
        return acceptService.getAcceptData(rawMachineId)
    }

    @GetMapping("/{rawMachineId}/photos/{photoNo}")
    fun getAcceptPhoto(
        @PathVariable("rawMachineId") rawMachineId: String,
        @PathVariable("photoNo") photoNo: Int,
        exchange: ServerWebExchange
    ): Mono<Void> {
        return Mono.just(exchange.response)
            .doOnNext { it.headers.contentType = MediaType.IMAGE_PNG }
            .zipWith(acceptService.getAcceptPhoto(rawMachineId, photoNo))
            .flatMap { it.t1.writeAndFlushWith(Mono.just(Mono.just(it.t1.bufferFactory().wrap(it.t2)))) }
            .then()
    }

    /**
     * 上传验收数据
     */
    @PostMapping("/{rawMachineId}")
    fun acceptDataUpload(
        @PathVariable rawMachineId: String,
        @ModelAttribute acceptParam: AcceptDataParam,
        @RequestPart("photos") photos: Flux<FilePart>,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return photos.map(FileUtils::templateSaveFile)
            .collectList()
            .flatMap { acceptService.handleAcceptData(rawMachineId, acceptParam.acceptData, it, auth.name) }
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 上传中段验收数据
     */
    @PostMapping("/mid/{rawMachineId}")
    fun acceptMidCheckData(
        @PathVariable rawMachineId: String,
        @ModelAttribute acceptParam: AcceptDataParam,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return acceptService.handleMidAcceptData(rawMachineId, acceptParam.acceptData, auth.name)
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 进行了自我检查
     */
    @PutMapping("/{rawMachineId}/selfChecked")
    fun selfChecked(@PathVariable("rawMachineId") rawMachineId: String): Mono<InvokeResultDto> {
        return acceptService.handleAcceptSelfChecked(rawMachineId)
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 修改数据
     */
    @PostMapping("/modify/{rawMachineId}")
    fun modifyAcceptDataUpload(
        @PathVariable rawMachineId: String,
        @ModelAttribute acceptParam: AcceptDataParam,
        @RequestPart("photos") photos: Flux<FilePart>,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return photos.map(FileUtils::templateSaveFile)
            .collectList()
            .flatMap { acceptService.handleModifyAcceptData(rawMachineId, acceptParam.acceptData, it, auth.name) }
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

}