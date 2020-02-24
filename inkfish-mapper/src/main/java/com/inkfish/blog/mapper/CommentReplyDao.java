package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.CommentReply;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface  CommentReplyDao extends BaseMapper<CommentReply> {
}
