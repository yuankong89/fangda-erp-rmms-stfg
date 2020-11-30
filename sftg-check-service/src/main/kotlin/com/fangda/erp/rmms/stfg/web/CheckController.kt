package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.accept.AcceptedMachineDto
import com.fangda.erp.rmms.stfg.dto.accept.MidAcceptedMachineDto
import com.fangda.erp.rmms.stfg.service.AcceptService
import com.fangda.erp.rmms.stfg.service.MachineService
import com.fangda.erp.rmms.stfg.web.param.check.CheckResultParam
import com.fangda.erp.rmms.stfg.web.param.check.MidAcceptedSearchParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/11/23
 */
@RestController
@RequestMapping("/check")
@CrossOrigin
class CheckController @Autowired constructor(
    private val machineService: MachineService,
    private val acceptService: AcceptService
) {
    /**
     * 获得所有验收完成的车辆
     */
    @GetMapping("/machines")
    fun listAcceptedMachines(): Mono<List<AcceptedMachineDto>> {
        return machineService.listAcceptedMachines()
    }

    /**
     * 获得中段验收完成的车辆
     */
    @GetMapping("/midMachines")
    fun listMidAcceptedMachines(
        @ModelAttribute midAcceptedSearchParam: MidAcceptedSearchParam
    ): Mono<List<MidAcceptedMachineDto>> {
        return machineService.listMidAcceptedMachines(midAcceptedSearchParam.startTime, midAcceptedSearchParam.endTime)
    }

    /**
     * 上传审核结果
     */
    @PostMapping("/{rawMachineId}")
    fun check(
        @PathVariable("rawMachineId") rawMachineId: String,
        @ModelAttribute param: CheckResultParam,
        authentication: Authentication
    ): Mono<InvokeResultDto> {
        return acceptService.handleCheck(rawMachineId, param.result, param.comment, authentication.name)
            .map { InvokeResultDto.successResult("操作成功") }
            .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
    }
}