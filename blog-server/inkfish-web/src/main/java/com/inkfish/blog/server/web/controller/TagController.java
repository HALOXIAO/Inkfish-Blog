package com.inkfish.blog.server.web.controller;

import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.mapper.convert.ArticleToArticleOverviewVO;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.service.ArticleTagService;
import com.inkfish.blog.server.service.UserBehaviorService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author HALOXIAO
 **/

@Api("标签相关的操作")
@RestController
@Slf4j
public class TagController {

    @Autowired
    ArticleTagService articleTagService;

    @Autowired
    UserBehaviorService userBehaviorService;

    @GetMapping("/tag/all")
    public ResultBean<List<String>> allTags() {
        List<ArticleTag> list = articleTagService.getAllTags();
        if (list == null) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        List<String> resultList = new ArrayList<>(list.size());
        for (ArticleTag tag : list) {
            resultList.add(tag.getName());
        }
        ResultBean<List<String>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(resultList);
        return bean;
    }

    @GetMapping("/tag/{tag}")
    public ResultBean<List<ArticleOverviewVO>> getTag(@PathVariable("tag") String tag) {
        List<Article> articles = articleTagService.getArticle(tag);
        List<ArticleOverviewVO> resultList = ArticleToArticleOverviewVO.INSTANCE.toArticleOverviewVOList(articles);

        return null;
    }

}
