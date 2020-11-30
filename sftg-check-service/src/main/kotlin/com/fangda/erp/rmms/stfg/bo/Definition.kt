package com.fangda.erp.rmms.stfg.bo

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/25
 */
class Definition {
    var defType: String by Delegates.notNull()
    var defData: String by Delegates.notNull()
    var defVersion: Int by Delegates.notNull()
}