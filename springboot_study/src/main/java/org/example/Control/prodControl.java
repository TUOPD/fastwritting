package org.example.Control;

import org.example.pojo.BaseConfig;
import org.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@Profile("prod")
public class prodControl {
    private final BaseConfig baseConfig;

    // 2. 使用构造函数进行注入 (这是Spring推荐的最佳实践)
    // Spring会自动找到它管理的 BaseConfig Bean，并把它作为参数传进来
    @Autowired
    public prodControl(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }
    @GetMapping("/getConfig")
    public Result<BaseConfig> getconfig(){
               return new Result<>(200,"成功",this.baseConfig);
    }
}
