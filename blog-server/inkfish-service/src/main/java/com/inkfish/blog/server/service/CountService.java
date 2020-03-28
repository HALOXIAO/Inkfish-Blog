package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.server.common.exception.DBTransactionalException;
import com.inkfish.blog.server.mapper.CountMapper;
import com.inkfish.blog.server.model.pojo.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HALOXIAO
 **/
@Component
public class CountService {

    private final CountMapper countMapper;

    @Autowired
    public CountService(CountMapper countMapper) {
        this.countMapper = countMapper;
    }

    @Transactional(rollbackFor = DBTransactionalException.class, propagation = Propagation.MANDATORY)
    protected void addArticleCount() {
        if (!countMapper.getBaseMapper().articleIncre()) {
            throw new DBTransactionalException("inc article total error");
        }

    }

    @Transactional(rollbackFor = DBTransactionalException.class, propagation = Propagation.MANDATORY)
    protected void addTagCount() {
        if (!countMapper.getBaseMapper().tagIncre()) {
            throw new DBTransactionalException("inc tag total error");
        }
    }

    public Integer getArticleCount() {
        Count count = countMapper.getOne(new QueryWrapper<Count>().select("article_total").eq("id", 0));
        return count.getArticleTotal();
    }

    public Integer getTagCount() {
        Count count = countMapper.getOne(new QueryWrapper<Count>().select("tag_total").eq("id", 0));
        return count.getTagTotal();
    }


}
