package com.inkfish.blog.server.model.vo;

/**
 * @author HALOXIAO
 **/
public class PanelViewsVO {

    private String title;
    private Integer views;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "PanelViewsVO{" +
                "title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}
