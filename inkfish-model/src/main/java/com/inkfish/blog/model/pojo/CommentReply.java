package com.inkfish.blog.model.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.Null;

@TableName("comment_reply")
public class CommentReply {
    @Null
    @TableId( value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("comment_id")
    private Integer commentId;

    @TableField("content")
    private String content;

    @TableField("target_id")
    private Integer targetId;

    @TableField
    private String targetName;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentId() {
        return this.commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTargetId() {
        return this.targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return "CommentReply{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                ", targetId=" + targetId +
                ", targetName='" + targetName + '\'' +
                '}';
    }
}
