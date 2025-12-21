package org.example.Dao;

import org.example.Mapper.ArticleNoteMapper;
import org.example.pojo.Articlenote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
@Profile("prod")
@Repository
public class ArticleNoteDao {
    @Autowired
    private ArticleNoteMapper articleNoteMapper;

    public List<Articlenote> getArticleNotes(){
        return articleNoteMapper.getlistArticles();
    }
    public int InsertArticle(Articlenote articleNote){
        return articleNoteMapper.insert(articleNote);
    }
    public int DeleteByid(BigInteger id){
        return articleNoteMapper.deleteById(id);
    }
    public int updateArticlenote(Articlenote articlenote){
        return  articleNoteMapper.updateById(articlenote);
    }
}
