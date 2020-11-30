package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.customize.DefinitionDto
import com.fangda.erp.rmms.stfg.dto.customize.StfgLevelDto
import com.fangda.erp.rmms.stfg.dto.customize.StfgTypeDto
import com.fangda.erp.rmms.stfg.service.FlowService
import com.fangda.erp.rmms.stfg.web.param.customize.DefineFormParam
import com.fangda.erp.rmms.stfg.web.param.customize.DefineTypesParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/11/23
 */
@RestController
@RequestMapping("/customize")
class CustomizeController @Autowired constructor(
    private val flowService: FlowService
) {
    /**
     * 应用启动时自动加载并保存
     * 根据[typeCode]，获得物资的验收表单
     */
    @GetMapping("/accept/form")
    fun getAcceptForm(typeCode: String): Mono<DefinitionDto> {
        return flowService.getAcceptFormByTypeCode(typeCode)
    }

    /**
     * 应用启动时自动加载并保存
     * 获得所有类型对应的物资
     * 返回Json类型
     */
    @GetMapping("/accept/type")
    fun getAcceptType(): Mono<DefinitionDto> {
        return flowService.getAcceptType()
    }

    /**
     * 获取生铁废钢的所有类型编码
     */
    @GetMapping("/stfg/type")
    fun listStfgTypes(): Mono<List<StfgTypeDto>> {
        return flowService.getStfgTypes()
    }

    @GetMapping("/stfg/level")
    fun listStfgLevel(): Mono<List<StfgLevelDto>> {
        return flowService.getStfgLevels()
    }

    /**
     * 定义验收类型, 如:
     * ST: (1000123,1002),(1000123,1002)
     * FG: (1000123,1002),(1000123,1002)
     * QT: (1000123,1002),(1000123,1002)
     */
    @PostMapping("/types")
    fun defineTypes(@ModelAttribute defineTypesParam: DefineTypesParam): Mono<InvokeResultDto> {
        return if (defineTypesParam.data.isNotEmpty()) {
            flowService.defineTypes(defineTypesParam.data)
                .map { InvokeResultDto.successResult("操作成功") }
                .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
        } else {
            Mono.just(InvokeResultDto.failResult("未传入参数"))
        }
    }

    /**
     * 定义验收表单, 如:
     * (1000123,1002) -> {验收表单}
     * (1000231,1001) -> {验收表单}
     */
    @PostMapping("/forms")
    fun defineTypeForm(@ModelAttribute defineFormParam: DefineFormParam): Mono<InvokeResultDto> {
        return if (defineFormParam.typeCode.isNotEmpty() && defineFormParam.formDefineData.isNotEmpty()) {
            flowService.defineForm(defineFormParam.typeCode, defineFormParam.levelCode, defineFormParam.formDefineData)
                .map { InvokeResultDto.successResult("操作成功") }
                .onErrorResume(BusinessException::class.java) { Mono.just(InvokeResultDto.failResult(it.message!!)) }
        } else {
            Mono.just(InvokeResultDto.failResult("未传入参数"))
        }
    }
}