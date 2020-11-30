/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

plugins {
    `java-library`
    `maven-publish`
}

version = "0.0.1-beta"

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
