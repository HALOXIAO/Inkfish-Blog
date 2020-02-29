package com.inkfish.blog.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@ApiModel("文章实体")
public class ArticleVO {
    private String title;
    private String content;
    private String image;
    private List<String> tags;
    private Integer vote;
    private Integer watch;
    private Timestamp createTime;
    private Timestamp updateTime;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Integer getWatch() {
        return watch;
    }

    public void setWatch(Integer watch) {
        this.watch = watch;
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

    @Override
    public String toString() {
        return "ArticleVO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", tags=" + tags +
                ", vote=" + vote +
                ", watch=" + watch +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}