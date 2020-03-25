package com.inkfish.blog.server.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author HALOXIAO
 **/

@ApiModel("文章预览实体")
public class ArticleOverviewVO {

    @ApiModelProperty("文章的Id，透过这个Id来访问文章")
    private Integer id;

    @ApiModelProperty("文章的前言，或者说大体内容")
    private String overview;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("创建文章的时间，格式为2019-03-02")
    private String createTime;

    @ApiModelProperty("更新文章的时间，格式为2019-03-02")
    private String updateTime;

    @ApiModelProperty("文章的标签")
    private List<String> tags;

    @ApiModelProperty("点赞次数")
    private Integer likes;

    @ApiModelProperty("浏览次数")
    private Integer views;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "ArticleOverviewVO{" +
                "id=" + id +
                ", overview='" + overview + '\'' +
                ", title='" + title + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", tags=" + tags +
                ", likes=" + likes +
                ", views=" + views +
                '}';
    }
}
