package com.renyujie.myeasyblog.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName MyBatisPlusConfig.java
 * @Description mybatisplus的设置类
 * @createTime 2021年12月02日 19:52:00
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.renyujie.myeasyblog.mapper")
public class MyBatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
