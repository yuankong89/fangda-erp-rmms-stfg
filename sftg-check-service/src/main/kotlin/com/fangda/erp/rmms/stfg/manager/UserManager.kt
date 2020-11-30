package com.fangda.erp.rmms.stfg.manager

import com.fangda.erp.rmms.stfg.bo.user.UserRole
import com.fangda.erp.rmms.stfg.bo.user.UserShift
import com.fangda.erp.rmms.stfg.db.dao.UserDao
import com.fangda.erp.rmms.stfg.db.dataobject.UserRoleDO
import com.fangda.erp.rmms.stfg.db.dataobject.UserShiftDO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author yuhb
 * @date 2020/11/24
 */
@Service
class UserManager @Autowired constructor(
    private val userDao: UserDao
) {
    /**
     * 获得用户的班次
     */
    fun listUserShift(userId: String): List<UserShift> {
        return userDao.listUserShiftByUserId(userId).map(this::convertUserShiftDOToBo)
    }

    /**
     * 获得用户可用界面
     */
    fun listUserRoles(userId: String): List<UserRole> {
        return userDao.listUserRolesByUserId(userId).map(this::convertUserRoleDOToBo)
    }

    // ---- private ----
    private fun convertUserShiftDOToBo(userShiftDO: UserShiftDO): UserShift {
        return UserShift().apply {
            this.role = UserShift.Role.values().find {
                it.value == userShiftDO.role
            }!!
            this.shift = UserShift.Shift.values().find {
                it.value == userShiftDO.shift
            }!!
        }
    }

    private fun convertUserRoleDOToBo(userRoleDO: UserRoleDO): UserRole {
        return UserRole().apply {
            this.roleId = userRoleDO.roleId
            this.roleName = userRoleDO.roleName
        }
    }
}