package com.inkfish.blog.server.model.vo;

import io.swagger.annotations.Api;
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

    @ApiModelProperty("创建文章的时间，格式为2019-03-02")
    private Timestamp createTime;

    @ApiModelProperty("更新文章的时间，格式为2019-03-02")
    private Timestamp updateTime;

    @ApiModelProperty("文章的标签")
    private List<String> tags;

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ArticleOverview{" +
                "id=" + id +
                ", overview='" + overview + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tags=" + tags +
                '}';
    }
}
