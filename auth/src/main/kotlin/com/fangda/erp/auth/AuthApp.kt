/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

/**
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

/**
 * 提供统一认证的接口
 * 提供统一授权的接口
 * @author yhb
 * @date 2020/05/21
 */
@SpringBootApplication
@EnableEurekaClient
open class AuthApp

fun main(args: Array<String>) {
    runApplication<AuthApp>(*args)
}