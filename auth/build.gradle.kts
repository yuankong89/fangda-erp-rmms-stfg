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
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-config-client")
    // feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // health
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // security
    implementation("org.springframework.cloud:spring-cloud-starter-security")
    // orm
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.2")
    implementation("com.oracle.database.jdbc:ojdbc8:19.7.0.0")
    implementation("com.oracle.database.nls:orai18n:19.7.0.0")

    implementation("com.auth0:java-jwt:3.10.3")
    implementation(project(":authlibs"))
    implementation(project(":erplibs"))
}

application {
    mainClass.set("com.fangda.erp.auth.AuthAppKt")
}
