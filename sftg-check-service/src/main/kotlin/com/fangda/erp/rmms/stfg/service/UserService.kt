package com.fangda.erp.rmms.stfg.service

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.rmms.stfg.bo.user.UserRole
import com.fangda.erp.rmms.stfg.bo.user.UserShift
import com.fangda.erp.rmms.stfg.dto.user.UserRoleDto
import com.fangda.erp.rmms.stfg.manager.UserManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author yuhb
 * @date 2020/11/24
 */
@Service
class UserService @Autowired constructor(
    private val userManager: UserManager
) {
    /**
     * 获得用户的班次
     * 根据系统当前时间判断用户的界面列表
     */
    fun listUserRoles(principal: FangdaPrincipal): Mono<List<UserRoleDto>> {
        return Mono.just(principal.userId)
            .flatMap { userId ->
                if (MANAGER_USER_ID.contains(userId)) {
                    Mono.fromCallable { userManager.listUserShift(principal.userId) }
                        .flatMapMany { shifts -> Flux.fromIterable(shifts) }
                        .any(this::isShiftMatchCurrentTime)
                        .flatMap { Mono.fromCallable { userManager.listUserRoles(principal.userId) } }
                        .switchIfEmpty(Mono.fromCallable {
                            userManager.listUserRoles(principal.userId).filter {
                                it.roleName != "生铁废钢验收角色" && it.roleName != "生铁废钢验收审核"
                            }
                        })
                } else {
                    Mono.fromCallable { userManager.listUserRoles(principal.userId) }
                }
            }
            .flatMapMany { Flux.fromIterable(it) }
            .map(this::convertUserRoleBOToDto)
            .collectList()
    }

    // ---- private ----
    /**
     * 获得当前时间所属的班次
     * 8:00 - 18:00 早班
     * 16:00 - 次日1:00 晚班
     */
    private fun isShiftMatchCurrentTime(userShift: UserShift): Boolean {
        when (userShift.shift) {
            UserShift.Shift.MORNING -> {
                val now = LocalDateTime.now()
                val today = now.toLocalDate()
                val start = LocalDateTime.of(today, LocalTime.of(8, 0))
                val end = LocalDateTime.of(today, LocalTime.of(18, 0))
                return now.isAfter(start) && now.isBefore(end)
            }
            UserShift.Shift.MIDDLE -> {
                val now = LocalDateTime.now()
                if (now.hour == 0) {
                    return true
                }
                val today = now.toLocalDate()
                val start = LocalDateTime.of(today, LocalTime.of(16, 0))
                val end = LocalDateTime.of(today.plusDays(1), LocalTime.of(1, 0))
                return now.isAfter(start) && now.isBefore(end)
            }
            else -> return false
        }
    }

    private fun convertUserRoleBOToDto(userRole: UserRole): UserRoleDto {
        return UserRoleDto().apply {
            this.roleId = userRole.roleId
            this.roleName = userRole.roleName
        }
    }

    companion object {
        val MANAGER_USER_ID = arrayOf("OP000345", "OP000715")
    }
}