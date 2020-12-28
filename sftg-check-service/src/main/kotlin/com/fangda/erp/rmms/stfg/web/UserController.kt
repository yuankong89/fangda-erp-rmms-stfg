package com.fangda.erp.rmms.stfg.web

import com.fangda.erp.authlibs.principal.FangdaPrincipal
import com.fangda.erp.rmms.stfg.dto.user.UserRoleDto
import com.fangda.erp.rmms.stfg.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @author yuhb
 * @date 2020/11/24
 */
@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(
    private val userService: UserService
) {
    /**
     * 获得用户的角色
     */
    @GetMapping("/roles")
    fun listUserRoles(authentication: Authentication): Mono<List<UserRoleDto>> {
        return userService.listUserRoles(authentication.principal as FangdaPrincipal)
    }
}