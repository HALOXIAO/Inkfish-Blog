package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HALOXIAO
 **/

@Mapper
public interface ArticleTagDao extends BaseMapper<ArticleTag> {
}
