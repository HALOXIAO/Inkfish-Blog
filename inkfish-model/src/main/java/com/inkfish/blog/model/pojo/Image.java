package com.inkfish.blog.model.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName( "image")
public class Image {
    @TableId( "id")
    private Integer id;

    @TableField( "image_url")
    private String imageUrl;

    @TableField( "article_id")
    private Integer articleId;

    @TableField( "username")
    private String username;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getArticleId() {
        return this.articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        username =this.username;
    }
}
