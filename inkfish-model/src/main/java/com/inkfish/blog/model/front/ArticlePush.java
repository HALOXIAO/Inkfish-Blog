package com.inkfish.blog.model.front;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ArticlePush {


    @Length(min = 1,max = 40)
    @NotNull
    private String title;
    
    @NotNull
    @Max(1)
    @Min(0)
    private Integer enableComment;

    @NotNull
    @Length(min = 1,max = 20)
    private String categoryName;

    @NotNull
    @Length(max = 400)
    private String overview;


    @NotNull
    @Max(2)
    @Min(0)
    private Integer status;

    @NotNull
    @Length(max = 8300000)
    private String content;



    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEnableComment() {
        return this.enableComment;
    }

    public void setEnableComment(Integer enableComment) {
        this.enableComment = enableComment;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public String toString() {
        return "ArticlePush{" +
                "title='" + title + '\'' +
                ", enableComment=" + enableComment +
                ", categoryName='" + categoryName + '\'' +
                ", overview='" + overview + '\'' +
                ", status=" + status +
                ", content='" + content + '\'' +
                '}';
    }
}
