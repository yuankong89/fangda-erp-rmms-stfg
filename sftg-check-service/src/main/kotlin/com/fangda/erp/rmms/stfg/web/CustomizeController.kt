package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.libs.dto.InvokeResultDto
import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.rmms.stfg.dto.customize.DefinitionDto
import com.fangda.erp.rmms.stfg.dto.customize.StfgLevelDto
import com.fangda.erp.rmms.stfg.dto.customize.StfgTypeDto
import com.fangda.erp.rmms.stfg.service.FlowService
import com.fangda.erp.rmms.stfg.web.param.customize.DefineFormParam
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
     * 获得所有UI对应的参数
     * 返回Json类型
     */
    @GetMapping("/ui/params")
    fun getUIParams(): Mono<DefinitionDto> {
        return flowService.getUIParams()
    }

    /**
     * 获取生铁废钢的第一层表单，包含基本验收信息，和打级表单
     */
    @GetMapping("/accept/form")
    fun getAcceptForm(typeCode: String, levelCode: String): Mono<DefinitionDto> {
        return flowService.getAcceptFormByTypeCode(typeCode, levelCode)
    }

    /**
     * 获取生铁废钢的明细表单，包含明细验收项目
     */
    @GetMapping("/accept/form/detail")
    fun getAcceptForm(level: String): Mono<DefinitionDto> {
        return flowService.getAcceptDetailForm(level)
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