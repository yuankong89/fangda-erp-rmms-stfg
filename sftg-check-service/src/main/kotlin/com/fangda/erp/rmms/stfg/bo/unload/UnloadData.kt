package com.fangda.erp.rmms.stfg.bo.unload

import java.time.LocalDateTime

/**
 * @author yuhb
 * @date 2020/11/25
 */
class UnloadData {
    var unloadPos: String = ""
    lateinit var unloadDate: LocalDateTime
    var unloadRemark: String = ""
}