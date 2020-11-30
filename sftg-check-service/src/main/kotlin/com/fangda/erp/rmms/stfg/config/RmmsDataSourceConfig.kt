/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.rmms.stfg.config

import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import javax.sql.DataSource

/**
 * @author yhb
 */
@Configuration
@MapperScan(basePackages = ["com.fangda.erp.rmms"], sqlSessionTemplateRef = "rmmsSqlSessionTemplate")
class RmmsDataSourceConfig {

    @Bean(name = ["rmmsDataSource"])
    @ConfigurationProperties(prefix = "rmms.jdbc")
    fun rmmsDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    fun rmmsSqlSessionFactory(@Qualifier("rmmsDataSource") dataSource: DataSource): SqlSessionFactory {
        val factoryBean = SqlSessionFactoryBean()
        factoryBean.setDataSource(dataSource)
        factoryBean.setMapperLocations(*PathMatchingResourcePatternResolver()
                .getResources("classpath:mapping/*.xml"))
        return factoryBean.`object`!!
    }

    @Bean
    fun rmmsSqlSessionTemplate(@Qualifier("rmmsSqlSessionFactory") rmmsSqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return SqlSessionTemplate(rmmsSqlSessionFactory)
    }
}