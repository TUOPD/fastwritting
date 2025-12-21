package org.example.Control;

import org.example.Service.ArticleNoteService;
import org.example.pojo.Articlenote;
import org.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Profile("prod")
public class ArticleNoteController {
    @Autowired
    private ArticleNoteService articleNoteService;

    @GetMapping("/getallarticleNote")
    public List<Articlenote> getallarticleNote(){
        return articleNoteService.getarticleNote();
    }
    @PostMapping("/insertarticleNote")
    public Result insertarticle(@RequestBody Articlenote articleNote){
        boolean check= articleNoteService.insertArticleNote(articleNote);
        if (check){
            return new Result(200,"成功",null);
        }else {
            return new Result(400,"失败",null);
        }
    }

}
