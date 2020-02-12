package com.inkfish.blog.model.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("article_comment")
public class ArticleComment {
    @TableId("id")
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("article_id")
    private Integer articleId;

    @TableField("comment_body")
    private String commentBody;

    @TableField("comment_create_time")
    private java.sql.Timestamp commentCreateTime;

    @TableField("status")
    private Integer status;

    @TableField("url")
    private String url;


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

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
                ", articleId=" + articleId +
                ", commentBody='" + commentBody + '\'' +
                ", commentCreateTime=" + commentCreateTime +
                ", status=" + status +
                ", url='" + url + '\'' +
                '}';
    }
}
