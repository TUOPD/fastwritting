package org.example.Control;

import io.micrometer.observation.annotation.Observed;
import org.example.Service.ArticlesService;
import org.example.pojo.Articles;
import org.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@CrossOrigin
@RestController
@Profile("prod")
public class ArticlesController {
    @Autowired
    private ArticlesService articlesService;

    @PostMapping("/insertarticle")
    public Result insertarticle(@RequestBody Articles articles){
        boolean check = articlesService.insertArticle(articles);
        if (check){
            return new Result(200,"成功",null);
        }else {
            return new Result(400,"失败",null);
        }
    }
    @PostMapping("/getid")
    public Result<Articles> getbyid(@RequestBody Map<String,BigInteger> map){
        System.out.println(map.get("id"));
        System.out.println(articlesService.getArticleone(map.get("id")));
        return new Result<Articles>(200,"成功",articlesService.getArticleone(map.get("id")));
    }
    @PostMapping("/deleteid")
    public Result deleteid(@RequestBody Map<String,BigInteger> map){
        boolean check = articlesService.DeleteArticle(map.get("id"));
        if (check){
            return new Result(200,"删除成功",null);
        }else{
            return  new Result(400,"删除失败",null);
        }
    }
    @PostMapping("/updatearticle")
    public Result updatearticle(@RequestBody Articles articles){
        System.out.println(articles.toString());
        boolean check = articlesService.updateArticle(articles);
        if (check){
            return  new Result(200,"更新成功",null);
        }else {
            return  new Result(400,"更新失败",null);
        }
    }
}
