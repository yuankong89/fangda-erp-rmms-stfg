package com.fangda.erp.rmms.stfg.bo.user

/**
 * @author yuhb
 * @date 2020/11/24
 */
class UserShift {
    /**
     * 排班
     */
    enum class Shift(val value: Int) {
        MORNING(0), MIDDLE(1), NIGHT(2);
    }

    /**
     * 验收角色：卸车人员、验收人员、管理人员(自动化部)
     */
    enum class Role(val value: Int) {
        NONE(0), UNLOAD(5), ACCEPT(10), MANAGER(20);
    }

    lateinit var role: Role
    lateinit var shift: Shift
}