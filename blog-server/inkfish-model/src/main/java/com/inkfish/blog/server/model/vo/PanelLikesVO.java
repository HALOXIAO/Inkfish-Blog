package com.inkfish.blog.server.model.vo;

/**
 * @author HALOXIAO
 **/
public class PanelLikesVO {

    private String title;
    private Integer likes;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "PanelLikesVO{" +
                "title='" + title + '\'' +
                ", likes=" + likes +
                '}';
    }
}
