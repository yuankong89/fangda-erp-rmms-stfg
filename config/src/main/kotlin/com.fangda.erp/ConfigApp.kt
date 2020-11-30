/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

/**
 * @author yhb
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
open class ConfigApp

fun main(args : Array<String>){
    runApplication<ConfigApp>(*args)
}