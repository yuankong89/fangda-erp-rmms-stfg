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
    enum class Role(val value: Int, val roleName: String) {
        NONE(0, "无"), UNLOAD(5, "卸车人员"), ACCEPT(10, "验收人员"), MANAGER(20, "管理人员");
    }

    lateinit var role: Role
    lateinit var shift: Shift
}