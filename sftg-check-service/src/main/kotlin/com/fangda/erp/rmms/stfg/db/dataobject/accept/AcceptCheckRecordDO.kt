package com.fangda.erp.rmms.stfg.db.dataobject.accept

import kotlin.properties.Delegates

/**
 * @author yuhb
 * @date 2020/11/25
 */
class AcceptCheckRecordDO {
    var rawMachineId: String by Delegates.notNull()
    var origState: Int by Delegates.notNull()
    var closeState: Int by Delegates.notNull()
    var checkOperator: String by Delegates.notNull()
    var comment: String by Delegates.notNull()
}