package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.depack.DePackDataDto
import com.fangda.erp.rmms.stfg.dto.depack.DePackMachineDto
import com.fangda.erp.rmms.stfg.service.DePackService
import com.fangda.erp.rmms.stfg.utils.FileUtils
import com.fangda.erp.rmms.stfg.web.param.depack.DePackDataParams
import com.fangda.erp.rmms.stfg.web.param.depack.DePackSearchParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/11/10
 */
@RestController
@RequestMapping("/depacks")
@CrossOrigin
class DePackController @Autowired constructor(
    private val dePackService: DePackService
) {
    /**
     * 根据条件查询待解包车辆
     */
    @GetMapping("")
    fun listDePacksByCriteria(
        @ModelAttribute params: DePackSearchParams,
    ): Mono<List<DePackMachineDto>> {
        if (params.stateIn == null && params.supplierId == null) {
            return Mono.empty()
        }
        return dePackService.listDePackByCriteria(
            params.stateIn,
            params.supplierId,
            params.startTime,
            params.endTime
        )
    }

    /**
     * 查询车的解包数据
     */
    @GetMapping("/{rawMachineId}")
    fun getDePackData(@PathVariable("rawMachineId") rawMachineId: String): Mono<DePackDataDto> {
        return dePackService.getDePackData(rawMachineId)
    }

    /**
     * 选择一个车辆进行解包
     */
    @PostMapping("/{rawMachineId}/need")
    fun dePack(
        @PathVariable("rawMachineId") rawMachineId: String,
        @ModelAttribute params: DePackDataParams,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return dePackService.needDePack(rawMachineId, auth.name)
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 开始解包
     */
    @PostMapping("/{rawMachineId}/start")
    fun dePackStart(
        @PathVariable("rawMachineId") rawMachineId: String,
        @ModelAttribute params: DePackDataParams,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return dePackService.startDePack(rawMachineId, auth.name)
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 结束解包，提交解包数据
     * 上传解包照片
     * 以Json格式传递
     */
    @PostMapping("/{rawMachineId}/end")
    fun dePackEnd(
        @PathVariable("rawMachineId") rawMachineId: String,
        @ModelAttribute params: DePackDataParams,
        @RequestPart("photos") photos: Flux<FilePart>,
        auth: Authentication
    ): Mono<InvokeResultDto> {
        return photos.map(FileUtils::templateSaveFile)
            .collectList()
            .flatMap { dePackService.endDePack(rawMachineId, params.dePackData, it, auth.name) }
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

}