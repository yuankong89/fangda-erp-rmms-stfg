package com.fangda.erp.rmms.stfg.db.dataobject

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/25
 */
class DefinitionDO {
    var defType: String by Delegates.notNull()
    var defData: String by Delegates.notNull()
    var defVersion: Int by Delegates.notNull()
}