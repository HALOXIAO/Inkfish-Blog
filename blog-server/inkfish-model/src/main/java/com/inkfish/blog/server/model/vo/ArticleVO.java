package com.inkfish.blog.server.model.vo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@ApiModel("文章实体")
public class ArticleVO implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private Integer status;
    private Integer enableComment;
    private List<String> tags;
    private Integer vote;
    private Integer watch;
    private String createTime;
    private String updateTime;


    public Integer getEnableComment() {
        return enableComment;
    }

    public void setEnableComment(Integer enableComment) {
        this.enableComment = enableComment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ArticleVO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", enableComment=" + enableComment +
                ", tags=" + tags +
                ", vote=" + vote +
                ", watch=" + watch +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}