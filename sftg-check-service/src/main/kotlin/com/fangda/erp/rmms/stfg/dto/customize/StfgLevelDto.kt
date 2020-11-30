package com.fangda.erp.rmms.stfg.dto.customize

/**
 * @author yuhb
 * @date 2020/11/23
 */
class StfgLevelDto constructor() {
    constructor(levelName: String, levelCode: String) : this() {
        this.levelName = levelName
        this.levelCode = levelCode
    }

    var levelName: String = ""
    var levelCode: String = ""
}