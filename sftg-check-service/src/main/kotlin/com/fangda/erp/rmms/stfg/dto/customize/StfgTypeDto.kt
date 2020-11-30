package com.fangda.erp.rmms.stfg.dto.customize

/**
 * @author yuhb
 * @date 2020/11/23
 */
class StfgTypeDto constructor() {
    constructor(typeName: String, typeCode: String): this() {
        this.typeName = typeName
        this.typeCode = typeCode
    }

    var typeName: String = ""
    var typeCode: String = ""
}