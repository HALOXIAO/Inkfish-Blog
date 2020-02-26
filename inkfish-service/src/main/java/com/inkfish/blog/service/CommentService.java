package com.inkfish.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.mapper.ArticleCommentMapper;
import com.inkfish.blog.model.pojo.ArticleComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Service
public class CommentService {

    @Autowired
    ArticleCommentMapper articleCommentMapper;


    //TODO 可以改成WHERE的方式，这样耗时不会随着数据量的提升而提升
    public List<ArticleComment> getCommentWithPage(Integer articleId, Integer page, Integer size) {
        List<ArticleComment> lists = articleCommentMapper.getBaseMapper().getCommentPageWithArticle(articleId, page, size);
        if(lists!= null){
            lists.parallelStream().forEach(p->{p.setArticleId(articleId);});
            return lists;
        }
        return null;
    }

    public Boolean addComment(ArticleComment articleComment) {
        return articleCommentMapper.save(articleComment);
    }

    public Boolean deleteComment(Integer id) {
        return articleCommentMapper.remove(new QueryWrapper<ArticleComment>().eq("id", id));
    }

}
