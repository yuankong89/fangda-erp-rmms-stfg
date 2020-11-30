package com.fangda.erp.rmms.stfg.dto.unload

/**
 * @author yuhb
 * @date 2020/11/23
 */
class UnloadDataDto {
    var unloadPos: String = ""
    var unloadDate: String = ""
    var unloadRemark: String = ""
    lateinit var details: List<UnloadDataDetailDto>
}