package com.inkfish.blog.server.service.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author HALOXIAO
 **/
@Aspect
public class ArticleServiceAOP {


    /**
     *
     * */
    @Before(value = "execution(* com.inkfish.blog.server.service.ArticleService.addArticleWithTags()))")
    public void asd() {

    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.service.ArticleService.addArticleWithTags() )")
    public void asdasd() {

    }




//        TODO 转移ArticleService中的Cache操作

}
