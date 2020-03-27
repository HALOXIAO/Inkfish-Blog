package com.inkfish.blog.server.service.aop;

import com.inkfish.blog.server.common.REDIS_TAG_CACHE_NAMESPACE;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.service.ArticleService;
import com.inkfish.blog.server.service.ArticleTagService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@Aspect
@Component
public class ArticleServiceAOP {

    private final StringRedisTemplate stringRedisTemplate;
    private final ArticleService articleService;
    private final ArticleTagService articleTagService;

    @Autowired
    public ArticleServiceAOP(StringRedisTemplate stringRedisTemplate, ArticleService articleService, ArticleTagService articleTagService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.articleService = articleService;
        this.articleTagService = articleTagService;
    }

//    @AfterReturning(value = "execution(* com.inkfish.blog.server.service.ArticleService.addArticleWithTags(article,tags))&&args(article,tags)", returning = "flag", argNames = "article,tags,flag")
//    public void updateTagCount(Article article, List<ArticleTag> tags, boolean flag) {
//        if (flag) {
//
//        } else {
//
//        }
//    }



//        TODO 转移ArticleService中的Cache操作

}
