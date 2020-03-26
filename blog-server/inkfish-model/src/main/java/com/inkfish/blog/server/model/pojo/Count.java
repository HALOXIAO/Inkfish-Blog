package com.inkfish.blog.server.model.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("count")
public class Count {

    @TableId("id")
    private Integer id;

    @TableField("article_total")
    private Integer articleTotal;

    @TableField("tag_total")
    private Integer tagTotal;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleTotal() {
        return this.articleTotal;
    }

    public void setArticleTotal(Integer articleTotal) {
        this.articleTotal = articleTotal;
    }

    public Integer getTagTotal() {
        return this.tagTotal;
    }

    public void setTagTotal(Integer tagTotal) {
        this.tagTotal = tagTotal;
    }

    @Override
    public String toString() {
        return "Count{" +
                "id=" + id +
                ", articleTotal=" + articleTotal +
                ", tagTotal=" + tagTotal +
                '}';
    }
}
