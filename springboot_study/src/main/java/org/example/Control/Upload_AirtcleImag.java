package org.example.Control;


import org.example.pojo.Artcile_Oss;
import org.example.pojo.Result;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("prod")
@RequestMapping("/upload_artile")
@CrossOrigin
@RestController
public class Upload_AirtcleImag {
    @PostMapping("/artile_img")
    public Result upload_artile_img(  @RequestParam("files") List<MultipartFile> files, // 接收文件数组
                                      @RequestParam("localIds") List<String> localIds )
    {
        if (files.size() != localIds.size()) {
            return new Result(450,"文件与id数量不匹配",null);
        }

        List<Map<String, String>> results = new ArrayList<>();

        // 遍历数组，下标一一对应
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String localId = localIds.get(i);

            // 保存文件并获取真实 URL
            // String realUrl = fileStorageService.save(file);
            String realUrl = Artcile_Oss.upload(file,localId);

            // 构建返回的匹配关系
            Map<String, String> map = new HashMap<>();
            map.put("localId", localId);
            map.put("realUrl", realUrl);
            results.add(map);
        }

        // 返回包含所有匹配关系的列表
        return new Result(200,"成功",results);
    }
}