package com.inkfish.blog.server.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.mapper.convert.ArticleToArticleOverviewVO;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.model.vo.ArticleTagHomeVO;
import com.inkfish.blog.server.model.vo.ArticleTagVO;
import com.inkfish.blog.server.service.ArticleService;
import com.inkfish.blog.server.service.ArticleTagService;
import com.inkfish.blog.server.service.CountService;
import com.inkfish.blog.server.service.UserBehaviorService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    ArticleService articleService;

    @Autowired
    CountService countService;




    @GetMapping("/tag/all")
    public ResultBean<ArticleTagHomeVO> allTags(Integer page, Integer size) {
        if (page > 100) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        IPage<ArticleTag> ipage = articleTagService.getTagsWithPage(page, size);
        List<ArticleTag> list = ipage.getRecords();
        if (list == null) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        List<ArticleTagVO> resultList = Lists.transform(list, new Function<ArticleTag, ArticleTagVO>() {
            @Nullable
            @Override
            public ArticleTagVO apply(@Nullable ArticleTag input) {
                ArticleTagVO tagVO = new ArticleTagVO();
                tagVO.setId(input.getId());
                tagVO.setTagName(input.getName());
                return tagVO;
            }
        });
        ArticleTagHomeVO tagHome = new ArticleTagHomeVO();
        tagHome.setTagsList(resultList);
        tagHome.setTagTotal(countService.getTagCount());
        ResultBean<ArticleTagHomeVO> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(tagHome);
        return bean;
    }

    @GetMapping("/tag")
    public ResultBean<List<ArticleOverviewVO>> getTag(String tag) {
        List<Article> articles = articleTagService.getArticle(tag);
        List<ArticleOverviewVO> resultList = ArticleToArticleOverviewVO.INSTANCE.toArticleOverviewVOList(articles);
        resultList = articleTagService.setTagsForArticleOverviewVO(resultList);
        resultList = articleService.addArticleOverviewVOLikesAndWatchList(resultList);
        ResultBean<List<ArticleOverviewVO>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(resultList);
        return bean;
    }

    @PostMapping("/tag")
    public ResultBean<Boolean> addTags(List<String> tagsName) {
        if (null == articleTagService.addTags(tagsName)) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
    }

    @DeleteMapping("/tag")
    public ResultBean<Boolean> deleteTag(Integer tagId) {
        if (!articleTagService.deleteArticleTag(tagId)) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
    }

    @GetMapping("/tag/select")
    public ResultBean<List<String>> getSelectTag(Integer page) {
        final Integer size = 20;
        IPage<ArticleTag> iPage = articleTagService.getTagsNameWithPage(page, size);
        List<ArticleTag> list = iPage.getRecords();
        List<String> result = Lists.transform(list, new Function<ArticleTag, String>() {
            @Nullable
            @Override
            public String apply(@Nullable ArticleTag input) {
                return input.getName();
            }
        });
        ResultBean<List<String>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(result);
        return bean;
    }


}
