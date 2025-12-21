package org.example.Service;

import org.example.Dao.ArticleNoteDao;
import org.example.pojo.Articlenote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Profile("prod")
@Service
public class ArticleNoteService {
    @Autowired
    private ArticleNoteDao articleNoteDao;

    public List<Articlenote> getarticleNote(){
        return articleNoteDao.getArticleNotes();
    }
    public boolean insertArticleNote(Articlenote articleNote){
        LocalDate localDate=LocalDate.now();
        articleNote.setUpdatetime(localDate);
        int cout = articleNoteDao.InsertArticle(articleNote);
        if (cout == 1){
            return true;
        }else {
            return false;
        }

    }
}
