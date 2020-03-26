package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.common.exception.DBTransactionalException;
import com.inkfish.blog.server.model.pojo.Count;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HALOXIAO
 **/

@Repository
public class CountMapper extends ServiceImpl<CountDao, Count> {

    @Transactional(rollbackFor = DBTransactionalException.class, propagation = Propagation.MANDATORY)
    public void addArticleCount() {
        if (!this.getBaseMapper().articleIncre()) {
            throw new DBTransactionalException("inc article total error");
        }

    }

    @Transactional(rollbackFor = DBTransactionalException.class, propagation = Propagation.MANDATORY)
    public void addTagCount() {
        if (!this.getBaseMapper().tagIncre()) {
            throw new DBTransactionalException("inc tag total error");
        }
    }

    @Transactional(rollbackFor = DBTransactionalException.class, propagation = Propagation.MANDATORY)
    public void decrArticleCount() {
        if (!this.getBaseMapper().articleDece()) {
            throw new DBTransactionalException("inc tag total error");
        }
    }

    @Transactional(rollbackFor = DBTransactionalException.class, propagation = Propagation.MANDATORY)
    public void decrTagCount() {
        if (!this.getBaseMapper().tagDece()) {
            throw new DBTransactionalException("inc tag total error");
        }
    }

}
