package com.inkfish.blog.server.service.manager;

import com.inkfish.blog.server.model.vo.ArticleVO;
import com.inkfish.blog.server.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

/**
 * @author HALOXIAO
 **/
@Service
public class TagManager {

    private final ArticleTagService articleTagService;

    @Autowired
    public TagManager(ArticleTagService articleTagService) {
        this.articleTagService = articleTagService;
    }

}
