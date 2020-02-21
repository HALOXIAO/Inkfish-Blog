package com.inkfish.blog.model.vo;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author HALOXIAO
 **/
public class ArticleOverviewVO {

    private Integer id;
    private String overview;
    private Timestamp createTime;
    private Timestamp updateTime;
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
