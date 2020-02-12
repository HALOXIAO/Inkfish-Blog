package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.ArticleAndTagRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HALOXIAO
 **/
@Component
public class ArticleTagRelationMapper extends ServiceImpl<ArticleTagRelationDao,ArticleAndTagRelation> {
}
