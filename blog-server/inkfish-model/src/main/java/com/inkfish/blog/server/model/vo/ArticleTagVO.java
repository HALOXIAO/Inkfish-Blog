package com.inkfish.blog.server.model.vo;

/**
 * @author HALOXIAO
 **/
public class ArticleTagVO {
    private Integer id;

    private String tagName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "ArticleTagVO{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
