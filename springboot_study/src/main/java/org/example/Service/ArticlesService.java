package org.example.Service;

import org.example.Dao.AirticlesDao;
import org.example.Dao.ArticleNoteDao;
import org.example.pojo.Articlenote;
import org.example.pojo.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
@Profile("prod")
@Service
public class ArticlesService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    @Autowired
    private AirticlesDao airticlesDao;
    @Autowired
    private ArticleNoteDao articleNoteDao;

    public Articles getArticleone(BigInteger id){
        return airticlesDao.getAirticleOne(id);
    }
    public boolean insertArticle(Articles articles){
        LocalDate localDate = LocalDate.now();
        int randomPart = 100 + new Random().nextInt(900);

        BigInteger id = new BigInteger( LocalDate.now().format(DATE_FORMATTER)+randomPart);
        articles.setCreatetime(localDate);
        articles.setId(id);
       int coutnote =  articleNoteDao.InsertArticle(new Articlenote(id,articles.getTitle(),localDate));
        int count= airticlesDao.insertArticleone(articles);
        if (count == 1 && coutnote == 1){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean DeleteArticle(BigInteger id){
        int cout1 = articleNoteDao.DeleteByid(id);
        int cout2 = airticlesDao.DeletByid(id);
        if(cout2 ==1 && cout1 == 1){
            return  true;
        }else {
            return  false;
        }
    }
    public boolean updateArticle(Articles articles){
        System.out.println("----------service----------");
        System.out.println(articles);
        Articlenote articlenote = new Articlenote(articles.getId(),articles.getTitle(),LocalDate.now());
        articles.setUpdatetime(LocalDate.now());
        int cout1 = airticlesDao.updateArticleone(articles);
        int cout2 = articleNoteDao.updateArticlenote(articlenote);
        System.out.println(cout2+"     "+cout1);
        if (cout2 == 1 && cout1 == 1){
            return true;
        }else {
            return  false;
        }
    }
}
