package org.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Articles;
import org.springframework.context.annotation.Profile;

import java.math.BigInteger;
//@Profile("prod")
@Mapper
public interface ArticlesMapper extends BaseMapper<Articles> {
    @Select("SELECT * FROM Articles WHERE id = #{id}")
    public Articles getairticleon(BigInteger id);
}
