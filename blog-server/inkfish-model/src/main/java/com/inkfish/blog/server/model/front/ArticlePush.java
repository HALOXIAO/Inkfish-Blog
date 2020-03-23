package com.inkfish.blog.server.model.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("文章实体")
public class ArticlePush {


    @ApiModelProperty(
            required = false,
            value = "文章Id"
    )
    private Integer id;

    @ApiModelProperty(
            required = true,
            value = "文章标题，范围:1-40"
    )
    @Length(min = 1, max = 40)
    @NotNull
    private String title;

    @ApiModelProperty(
            required = true,
            value = "是否开启评论区，1为开启，0为关闭，范围：0 or 1"
    )
    @NotNull
    @Max(1)
    @Min(0)
    private Integer enableComment;


    @ApiModelProperty(
            required = true,
            value = "分类信息，范围：1-20"
    )
    @NotNull
    @Length(min = 1, max = 20)
    private String categoryName;

    @ApiModelProperty(
            required = true,
            value = "文章的预览 范围：0-400"
    )
    @NotNull
    @Length(max = 400)
    private String overview;


    @ApiModelProperty(
            required = true,
            value = "文章的状态，0为草稿，1为完成品"
    )
    @NotNull
    @Max(1)
    @Min(0)
    private Integer status;

    @ApiModelProperty(
            required = true,
            value = "正文，范围：0~8300000"
    )
    @NotNull
    @Length(max = 8300000)
    private String content;


    public Integer getId() {
        return id;
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
                "id=" + id +
                ", title='" + title + '\'' +
                ", enableComment=" + enableComment +
                ", categoryName='" + categoryName + '\'' +
                ", overview='" + overview + '\'' +
                ", status=" + status +
                ", content='" + content + '\'' +
                '}';
    }
}
