package com.fangda.erp.rmms.stfg.dto

/**
 * @author yuhb
 * @date 2020/11/10
 */
class DePackDataDto {
    var rawMachineId: String = ""
    var dePackPhotoCount: Int = 0
    lateinit var details: List<DePackDataDetailDto>
}