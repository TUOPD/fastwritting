package org.example.Dao;


import org.example.Mapper.ArticlesMapper;
import org.example.pojo.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
@Profile("prod")
@Repository
public class AirticlesDao {
    @Autowired
    private ArticlesMapper airticlesMapper;
    public Articles getAirticleOne(BigInteger id){
        return airticlesMapper.getairticleon(id);
    }
    public int insertArticleone(Articles articles){
        return airticlesMapper.insert(articles);
    }
    public int updateArticleone(Articles articles){
        return  airticlesMapper.updateById(articles);
    }
    public  int DeletByid(BigInteger id){
        return  airticlesMapper.deleteById(id);
    }
}
