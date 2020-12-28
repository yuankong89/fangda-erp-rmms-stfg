package com.fangda.erp.rmms.stfg.dto.accept

/**
 * @author yuhb
 * @date 2020/11/23
 */
class AcceptDataDto {
    var rawMachineId: String = ""
    var acceptPos: String = ""
    var acceptDate: String = ""
    var acceptRemark: String = ""
    lateinit var photoLinkList: List<String>
    lateinit var details: List<AcceptDataDetailDto>
}