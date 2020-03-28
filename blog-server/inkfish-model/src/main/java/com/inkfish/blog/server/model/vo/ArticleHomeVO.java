package com.inkfish.blog.server.model.vo;

import java.util.List;

/**
 * @author HALOXIAO
 **/
public class ArticleHomeVO {

    private List<ArticleOverviewVO> articleList;
    private Integer articlesTotal;


    public List<ArticleOverviewVO> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleOverviewVO> articleList) {
        this.articleList = articleList;
    }

    public Integer getArticlesTotal() {
        return articlesTotal;
    }

    public void setArticlesTotal(Integer articlesTotal) {
        this.articlesTotal = articlesTotal;
    }

    @Override
    public String toString() {
        return "ArticleHomeVO{" +
                "articleList=" + articleList +
                ", articlesTotal=" + articlesTotal +
                '}';
    }
}
