/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.rmms.stfg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author yhb
 * @date 2020/11/10
 */
@SpringBootApplication
open class DepackApp

fun main(args: Array<String>) {
    runApplication<DepackApp>(*args)
}