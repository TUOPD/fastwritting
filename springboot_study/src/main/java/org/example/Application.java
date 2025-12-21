package org.example; // 替换成您的包名

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 导入这三个类
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;


import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.mybatis.spring.annotation.MapperScan;

// 关键！exclude 的作用是：“在设置模式下，请彻底无视数据库相关的任何东西！”
@SpringBootApplication

public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);

    }
}
