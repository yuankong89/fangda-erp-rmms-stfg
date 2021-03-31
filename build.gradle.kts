/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("org.springframework.boot") version "2.3.5.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
}

repositories {
    maven {
        setUrl("http://maven.aliyun.com/nexus/content/groups/public/")
    }
    maven {
        setUrl("http://maven.aliyun.com/nexus/content/repositories/jcenter")
    }
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "com.fangda.erp.rmms.stfg"

    repositories {
        maven {
            setUrl("https://maven.aliyun.com/nexus/content/groups/public/")
        }
        maven {
            setUrl("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        }
        maven {
            setUrl("https://nexus.saas.hand-china.com/content/repositories/rdc/")
        }
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        // log
        implementation("org.springframework.boot:spring-boot-starter-log4j2")
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR8")
        }
    }

    configurations {
        implementation.get().exclude("org.springframework.boot", "spring-boot-starter-logging")
    }

    val compileKotlin: KotlinCompile by tasks
    compileKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }
    val compileTestKotlin: KotlinCompile by tasks
    compileTestKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }
}