package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.model.pojo.ArticleAndTagRelation;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author HALOXIAO
 **/
@Repository
public class ArticleTagRelationMapper extends ServiceImpl<ArticleTagRelationDao, ArticleAndTagRelation> {


    public String addArticleTagRelationProvider(Map<String, Object> map) {
        Integer articleId = (Integer) map.get("id");
        List<ArticleTag> tags = (List<ArticleTag>) map.get("tags");
        //INSERT INTO article_and_tag_relation (article_id,tag_id) SELECT 15,id FROM article_tag WHERE id=4
        return new SQL() {
            {
                INSERT_INTO("article_and_tag_relation (article_id,tag_id)");
                StringBuffer buffer = new StringBuffer("SELECT ");
                buffer.append(articleId).append(",id FROM article_tag WHERE name IN (");
                tags.stream().forEach(
                        p -> {
                            buffer.append(p.getName()).append(",");
                        }
                );
                buffer.deleteCharAt(buffer.lastIndexOf(","));
                buffer.append(")");
                INTO_COLUMNS(buffer.toString());
            }

        }.toString();
    }
}


