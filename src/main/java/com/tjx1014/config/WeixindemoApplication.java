package com.tjx1014.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import weixin.popular.config.WeixinConfig;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于14:27 2019/12/16
 **/
@SpringBootApplication
@ComponentScan(basePackages = "com.tjx1014")
public class WeixindemoApplication extends SpringBootServletInitializer {



    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(WeixindemoApplication.class);
    }

    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appsecret}")
    private String appsecret;

    @Value("${weixin.backUrl}")
    private String backUrl;




}
