package com.fangda.erp.rmms.stfg.service

import com.fangda.erp.libs.exception.BusinessException
import com.fangda.erp.libs.exception.PersistObjectException
import com.fangda.erp.rmms.stfg.bo.Definition
import com.fangda.erp.rmms.stfg.dto.customize.DefinitionDto
import com.fangda.erp.rmms.stfg.dto.customize.StfgLevelDto
import com.fangda.erp.rmms.stfg.dto.customize.StfgTypeDto
import com.fangda.erp.rmms.stfg.manager.DefinitionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/11/24
 */
@Service
class FlowService @Autowired constructor(
    private val definitionManager: DefinitionManager
) {
    /**
     * 获得验收时物资对应的类型
     * JSON格式如下：
     * ST：
     * FG：
     * YK：
     * QT：
     * ...
     */
    fun getAcceptType(): Mono<DefinitionDto> {
        return Mono.fromCallable { definitionManager.getDefinitionByDefType("AcceptType") }
            .map(this::convertDefinitionBOToDto)
    }

    /**
     * 通过物资编码获得验收表单
     */
    fun getAcceptFormByTypeCode(typeCode: String): Mono<DefinitionDto> {
        return Mono.fromCallable { definitionManager.getDefinitionByDefType(typeCode) }
            .map(this::convertDefinitionBOToDto)
    }

    /**
     * 获得所有的生铁废钢类型
     */
    fun getStfgTypes(): Mono<List<StfgTypeDto>> {
        return Mono.just(STFG_TYPES)
    }

    /**
     * 获得所有生铁废钢的等级
     */
    fun getStfgLevels(): Mono<List<StfgLevelDto>> {
        return Mono.just(STFG_LEVELS)
    }

    /**
     * 定义类型
     * [data]为Json格式
     * ST: (1000123,1002),(1000123,1002)
     * FG: (1000123,1002),(1000123,1002)
     * QT: (1000123,1002),(1000123,1002)
     * 直接存储数据，不做处理
     */
    fun defineTypes(data: String): Mono<Boolean> {
        return Mono.fromCallable { definitionManager.modifyDefinition("AcceptType", data) }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    /**
     * 定义表单
     * [formDefineData]为Json格式
     */
    fun defineForm(typeCode: String, levelCode: String, formDefineData: String): Mono<Boolean> {
        return Mono.fromCallable { definitionManager.modifyDefinition("($typeCode,$levelCode)", formDefineData) }
            .onErrorMap(PersistObjectException::class.java) { BusinessException(it.message ?: "未知错误") }
    }

    // ---- private ----
    private fun convertDefinitionBOToDto(definition: Definition): DefinitionDto {
        return DefinitionDto().apply {
            this.defData = definition.defData
            this.defType = definition.defType
            this.defVersion = definition.defVersion.toString()
        }
    }

    companion object {
        val STFG_TYPES: List<StfgTypeDto> = listOf(
            StfgTypeDto("生铁", "1002000071"),
            StfgTypeDto("废钢", "1002000072"),
            StfgTypeDto("压块", "1002000073"),
            StfgTypeDto("刨花压饼", "1002000156"),
            StfgTypeDto("破碎废钢", "1002000159"),
        )

        val STFG_LEVELS: List<StfgLevelDto> = listOf()
    }
}