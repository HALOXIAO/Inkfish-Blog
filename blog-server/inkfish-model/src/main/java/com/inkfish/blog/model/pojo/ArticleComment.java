package com.inkfish.blog.model.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@TableName("article_comment")
public class ArticleComment {

    @Null
    @TableId( value = "id",type = IdType.AUTO)
    private Integer id;

    @NotNull
    @TableField("user_id")
    private Integer userId;

    @TableField("username")
    private  String username;

    @NotNull
    @TableField("article_id")
    private Integer articleId;

    @NotNull
    @TableField("comment_body")
    private String commentBody;

    @Null
    @TableField("comment_create_time")
    private java.sql.Timestamp commentCreateTime;

    @Null
    @TableField("url")
    private String url;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArticleId() {
        return this.articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getCommentBody() {
        return this.commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public java.sql.Timestamp getCommentCreateTime() {
        return this.commentCreateTime;
    }

    public void setCommentCreateTime(java.sql.Timestamp commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
    }



    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ArticleComment{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", articleId=" + articleId +
                ", commentBody='" + commentBody + '\'' +
                ", commentCreateTime=" + commentCreateTime +
                ", url='" + url + '\'' +
                '}';
    }
}
