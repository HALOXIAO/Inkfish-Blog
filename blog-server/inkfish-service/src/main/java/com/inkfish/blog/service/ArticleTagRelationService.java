package com.inkfish.blog.service;

import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HALOXIAO
 **/
@Service
public class ArticleTagRelationService {
    @Autowired
    ArticleTagRelationMapper articleTagRelationMapper;

}
