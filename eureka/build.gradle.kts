/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

version="0.0.1-beta"

plugins {
    id("application")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
}

application {
    mainClass.set("com.fangda.erp.EurekaAppKt")
}