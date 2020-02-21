package com.inkfish.blog.model.dto;

import java.util.Comparator;

/**
 * @author HALOXIAO
 **/

public class
TagAndArticleDTO implements Comparable<TagAndArticleDTO>, Comparator<TagAndArticleDTO> {

    @Override
    public int compareTo(TagAndArticleDTO o) {
        return (this.articleId - o.articleId);
    }

    @Override
    public int compare(TagAndArticleDTO o1, TagAndArticleDTO o2) {
        return o1.articleId - o2.articleId;
    }

    private String name;

    private Integer articleId;

    public String getNames() {
        return name;
    }

    public void setNames(String name) {
        this.name = name;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }



    @Override
    public String toString() {
        return "TagAndArticleDTO{" +
                "name=" + name +
                ", articleId=" + articleId +
                '}';
    }
}
