package com.inkfish.blog.server.model.vo;

import java.util.List;

/**
 * @author HALOXIAO
 **/
public class ArticleTagHomeVO {


    private List<ArticleTagVO> tagsList;
    private Integer tagTotal;

    public List<ArticleTagVO> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<ArticleTagVO> tagsList) {
        this.tagsList = tagsList;
    }

    public Integer getTagTotal() {
        return tagTotal;
    }

    public void setTagTotal(Integer tagTotal) {
        this.tagTotal = tagTotal;
    }

    @Override
    public String toString() {
        return "ArticleTagHomeVO{" +
                "tagsList=" + tagsList +
                ", tagTotal=" + tagTotal +
                '}';
    }
}
