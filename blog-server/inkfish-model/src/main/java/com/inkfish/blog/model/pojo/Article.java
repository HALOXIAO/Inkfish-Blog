package com.inkfish.blog.model.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("article")
public class Article {
    @TableId( value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField( "title")
    private String title;

    @TableField( "enable_comment")
    private Integer enableComment;

    @TableField( "category_id")
    private Integer categoryId;

    @TableField("overview")
    private String overview;

    @TableField( "status")
    private Integer status;



    @TableField( "content")
    private String content;

    @TableField( "create_time")
    private java.sql.Timestamp createTime;

    @TableField( "update_time")
    private java.sql.Timestamp updateTime;

    public Integer getEnableComment() {
        return enableComment;
    }

    public void setEnableComment(Integer enableComment) {
        this.enableComment = enableComment;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }



    public java.sql.Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    public java.sql.Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(java.sql.Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", enableComment=" + enableComment +
                ", categoryId=" + categoryId +
                ", overview='" + overview + '\'' +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
