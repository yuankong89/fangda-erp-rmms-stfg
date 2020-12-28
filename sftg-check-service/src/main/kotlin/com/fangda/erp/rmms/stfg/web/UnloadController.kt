package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.unload.UnloadDataDto
import com.fangda.erp.rmms.stfg.dto.unload.UnloadedMachineDto
import com.fangda.erp.rmms.stfg.dto.unload.UnloadingMachineDto
import com.fangda.erp.rmms.stfg.service.AcceptService
import com.fangda.erp.rmms.stfg.service.MachineService
import com.fangda.erp.rmms.stfg.utils.FileUtils
import com.fangda.erp.rmms.stfg.web.param.unload.UnloadDataParam
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
@RequestMapping("/unload")
@CrossOrigin
class UnloadController @Autowired constructor(
    private val machineService: MachineService,
    private val acceptService: AcceptService
) {
    /**
     * @param typeCodes 物资编码 如:"1000123, 1000321"
     * @param levelCodes 物资等级编码
     * 获取所有等待卸车的车辆
     */
    @GetMapping("/machines")
    fun listUnloadingMachines(typeCodes: String, levelCodes: String): Mono<List<UnloadingMachineDto>> {
        return machineService.listUnloadingMachines(typeCodes, levelCodes)
    }

    /**
     * 可检查数据
     */
    @GetMapping("/check")
    fun listSelfCheckUnloadedMachines(authentication: Authentication): Mono<List<UnloadedMachineDto>> {
        return machineService.listSelfCheckUnloadedMachines(authentication.principal as FangdaPrincipal)
    }

    /**
     * 获得车辆验收数据
     */
    @GetMapping("/{rawMachineId}")
    fun getUnloadData(@PathVariable("rawMachineId") rawMachineId: String): Mono<UnloadDataDto> {
        return acceptService.getUnloadData(rawMachineId)
    }

    @GetMapping("/{rawMachineId}/photos/{photoNo}")
    fun getUnloadPhoto(
        @PathVariable("rawMachineId") rawMachineId: String,
        @PathVariable("photoNo") photoNo: Int,
        exchange: ServerWebExchange
    ): Mono<Void> {
        return Mono.just(exchange.response)
            .doOnNext { it.headers.contentType = MediaType.IMAGE_PNG }
            .zipWith(acceptService.getUnloadPhoto(rawMachineId, photoNo))
            .flatMap { it.t1.writeAndFlushWith(Mono.just(Mono.just(it.t1.bufferFactory().wrap(it.t2)))) }
            .then()
    }

    /**
     * 上传卸车数据
     */
    @PostMapping("/{rawMachineId}")
    fun unloadDataUpload(
        @PathVariable rawMachineId: String,
        @ModelAttribute unloadParam: UnloadDataParam,
        @RequestPart("photos") photos: Flux<FilePart>,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return photos.map(FileUtils::templateSaveFile)
            .collectList()
            .flatMap { acceptService.handleUnloadData(rawMachineId, unloadParam.unloadData, it, auth.name) }
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 进行了自我检查
     */
    @PutMapping("/{rawMachineId}/selfChecked")
    fun selfChecked(@PathVariable("rawMachineId") rawMachineId: String): Mono<InvokeResultDto> {
        return acceptService.handleUnloadAcceptSelfChecked(rawMachineId)
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 修改卸车数据
     */
    @PostMapping("/modify/{rawMachineId}")
    fun modifyUnloadData(
        @PathVariable rawMachineId: String,
        @ModelAttribute unloadParam: UnloadDataParam,
        @RequestPart("photos") photos: Flux<FilePart>,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return photos.map(FileUtils::templateSaveFile)
            .collectList()
            .flatMap { acceptService.handleModifyUnloadData(rawMachineId, unloadParam.unloadData, it, auth.name) }
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }
}