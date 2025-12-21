package org.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Articlenote;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Mapper
public interface ArticleNoteMapper extends BaseMapper<Articlenote> {
    @Select("SELECT * FROM ArticleNote")
    public List<Articlenote> getlistArticles();
}
