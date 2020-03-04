package com.inkfish.blog.server.service;

import com.inkfish.blog.server.mapper.ArticleTagRelationMapper;
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
