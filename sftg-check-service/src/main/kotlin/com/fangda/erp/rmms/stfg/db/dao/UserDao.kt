package com.fangda.erp.rmms.stfg.db.dao

import com.fangda.erp.rmms.stfg.db.dataobject.UserRoleDO
import com.fangda.erp.rmms.stfg.db.dataobject.UserShiftDO
import org.apache.ibatis.annotations.Mapper

/**
 * @author yuhb
 * @date 2020/11/24
 */
@Mapper
interface UserDao {
    // 获得用户的排班情况
    fun listUserShiftByUserId(userId: String): List<UserShiftDO>

    /**
     * 获得用户的角色
     */
    fun listUserRolesByUserId(userId: String): List<UserRoleDO>
}