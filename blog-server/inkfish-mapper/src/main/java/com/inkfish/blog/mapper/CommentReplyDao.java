package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.CommentReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface  CommentReplyDao extends BaseMapper<CommentReply> {

    @Select("SELECT id,username,target_id,target_name,content FROM comment_reply,WHERE comment_id=#{commentId},LIMIT #{page},#{size}")
    public List<CommentReply> getCommentReplyPage(Integer commentId,Integer page, Integer size);
}
