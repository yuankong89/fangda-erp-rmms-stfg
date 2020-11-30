package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.DePackDataDto
import com.fangda.erp.rmms.stfg.dto.DePackMachineDto
import com.fangda.erp.rmms.stfg.service.DePackService
import com.fangda.erp.rmms.stfg.web.param.DePackControllerParams
import com.fangda.erp.rmms.stfg.web.param.DePackSearchParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Files

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
     * 选择一个车辆进行解包
     */
    @PostMapping("/{rawMachineId}/need")
    fun dePack(
        @PathVariable("rawMachineId") rawMachineId: String,
        @ModelAttribute params: DePackControllerParams
    ): Mono<InvokeResultDto> {
        return dePackService.needDePack(rawMachineId, params.operator)
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

    /**
     * 开始解包
     */
    @PostMapping("/{rawMachineId}/start")
    fun dePackStart(
        @PathVariable("rawMachineId") rawMachineId: String,
        @ModelAttribute params: DePackControllerParams
    ): Mono<InvokeResultDto> {
        return dePackService.startDePack(rawMachineId, params.operator)
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
        @ModelAttribute params: DePackControllerParams,
        @RequestPart("photos") photos: Flux<FilePart>
    ): Mono<InvokeResultDto> {
        return photos.map {
            val tempFile = Files.createTempFile("tmp", it.filename())
            it.transferTo(tempFile)
            tempFile
        }
            .map { it.toFile() }
            .collectList()
            .flatMap { dePackService.endDePack(rawMachineId, params.dePackData, it, params.operator) }
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }

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
}