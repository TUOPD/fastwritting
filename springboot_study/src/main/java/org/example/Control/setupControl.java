package org.example.Control; // 建议为它创建一个新的包

// ... (导入必要的类，比如RestController, Autowired, Yaml等)
import org.example.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URI; // 用于 toURI() 方法
import java.net.URISyntaxException; // toURI() 方法会抛出这个异常
import java.net.URL;
@CrossOrigin
@RestController
@RequestMapping("/api")
@Profile("setup") // 关键！这句话表示这个类只在“设置模式”(setup)下才生效
public class setupControl {

    @Autowired
    private ConfigurableApplicationContext context;

    // 这个接口告诉前端：“嗨，我现在是设置模式，请显示配置页面。”
    @GetMapping("/status")
    public Map<String, Boolean> getStatus() {
        return Map.of("configured", false);
    }

    // 这个接口接收前端发来的数据库配置
    @PostMapping("/setup")
    public Map<String, Object> saveConfiguration(@RequestBody Map<String, String> config)  throws  Exception{
        // 1. 把前端传来的数据组装成一个Java Map
        System.out.println(config.toString());
        Map<String, Object> yamlData = new LinkedHashMap<>();
        // ... (省略了组装Map的代码，和上一个回答中一样) ...
        yamlData.put("server", Map.of("port", 9527));
        Map<String, Object> springMap = new LinkedHashMap<>();
        Map<String, Object> datasourceMap = new LinkedHashMap<>();
        datasourceMap.put("url", config.get("dbUrl"));
        datasourceMap.put("username", config.get("username"));
        datasourceMap.put("password", config.get("password"));
        datasourceMap.put("driver-class-name", "com.mysql.cj.jdbc.Driver");
        springMap.put("datasource", datasourceMap);
        yamlData.put("spring", springMap);
        yamlData.put("mybatis-plus", Map.of("mapper-locations", "classpath:/mapper/**/*.xml"));


        // 2. 用SnakeYAML工具把Map转成YAML格式，并写入“信号文件”
        try {
            // 1. 先调用方法获取目标目录
            String projectPath = System.getProperty("user.dir");
            String resourcesPath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources";
            File prodConfigFile = new File(resourcesPath, "application-prod.yml");
            // 目标 2: 主 application.yml
            File mainConfigFile = new File(resourcesPath, "application.yml");
            // 3. 准备 YAML 工具
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            // 4. 使用 try-with-resources 语句，将拼接好的文件对象传入 FileWriter
            try (FileWriter writer = new FileWriter(prodConfigFile)) {
                yaml.dump(yamlData, writer);
            }
            try (FileWriter writer = new FileWriter(prodConfigFile)) {
                yaml.dump(yamlData, writer);
            }
            // --- 操作 B: 修改主 application.yml 的 active profile (新增代码) ---
            if (mainConfigFile.exists()) {
                Map<String, Object> mainData;
                // 读取现有主配置
                try (InputStream input = new FileInputStream(mainConfigFile)) {
                    mainData = yaml.load(input);
                    if (mainData == null) {
                        mainData = new LinkedHashMap<>();
                    }
                }

                // 修改或创建 spring.profiles.active = prod
                // 使用 computeIfAbsent 确保嵌套结构存在
                Map<String, Object> spring = (Map<String, Object>) mainData.computeIfAbsent("spring", k -> new LinkedHashMap<String, Object>());
                Map<String, Object> profiles = (Map<String, Object>) spring.computeIfAbsent("profiles", k -> new LinkedHashMap<String, Object>());
                profiles.put("active", "prod");

                // 写回主配置
                try (FileWriter writer = new FileWriter(mainConfigFile)) {
                    yaml.dump(mainData, writer);
                }
            }
            } catch(IOException e){
                // 5. 使用一个 catch 块捕获多个异常，让代码更简洁
                //    在后台打印详细的错误堆栈，这对于调试至关重要！
                e.printStackTrace();

                // 6. 向前端返回一个有用的错误信息
                return Map.of("success", false, "message", "写入配置文件时发生错误: " + e.getMessage());
            }


            // 3. 触发重启
            restartApplication();
            return Map.of("success", true, "message", "配置成功，正在重启...");
        }
    private void restartApplication() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000L); // 等1秒，确保上面的HTTP响应发出去了
                SpringApplication.exit(context, () -> 0);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        thread.setDaemon(false);
        thread.start();
    }

}