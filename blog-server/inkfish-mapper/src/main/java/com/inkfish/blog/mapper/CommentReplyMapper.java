package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.CommentReply;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author HALOXIAO
 **/
@Repository
public class CommentReplyMapper extends ServiceImpl<CommentReplyDao, CommentReply> {
}
