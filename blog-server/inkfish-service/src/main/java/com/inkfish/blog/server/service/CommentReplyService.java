package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.server.mapper.CommentReplyMapper;
import com.inkfish.blog.server.model.pojo.CommentReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HALOXIAO
 **/

@Service
public class CommentReplyService {

    @Autowired
    CommentReplyMapper commentReplyMapper;


    public List<CommentReply> getCommentReplyPage(Integer commentId, Integer page, Integer size) {
//        commentReplyMapper.getBaseMapper().selectPage();
        return null;
    }

    public Boolean deleteCommentReply(Integer id) {
        return commentReplyMapper.remove(new QueryWrapper<CommentReply>().eq("id", id));
    }

    public Boolean pushCommentReply(CommentReply commentReply) {
        return commentReplyMapper.save(commentReply);
    }

}
