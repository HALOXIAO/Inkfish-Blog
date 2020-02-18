package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.ArticleAndTagRelation;
import com.inkfish.blog.model.pojo.ArticleTag;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Component
public class ArticleTagRelationMapper extends ServiceImpl<ArticleTagRelationDao, ArticleAndTagRelation> {

    public String addArticleTagRelationProvider(final Integer articleId, final List<ArticleTag> tags) {

       return new SQL() {
           {
               StringBuffer builder = new StringBuffer(tags.size());

               INSERT_INTO("article_and_tag_relation (article_id,tag_id)");
               tags.parallelStream().forEach(
                       p->{
                        INTO_VALUES(articleId.toString(),p.getName());
                        ADD_ROW();
                      }
               );
           }

        }.toString();

    }

}
