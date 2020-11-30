package com.fangda.erp.rmms.stfg.dto.accept

/**
 * @author yuhb
 * @date 2020/11/23
 */
class AcceptDataDto {
    var unloadPos: String = ""
    var checkData: String = ""
    var remark: String = ""
    lateinit var photoLinkList: List<String>
    lateinit var details: List<AcceptDataDetailDto>
}