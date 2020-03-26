package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author HALOXIAO
 **/
@Repository
public class ArticleTagMapper extends ServiceImpl<ArticleTagDao, ArticleTag> {


    public String getTagAndArticleDTOProvider(Map<String, Object> map) {
        List<Integer> id = (List<Integer>) map.get("idList");
        return new SQL() {
            {
                StringBuffer stringBuffer = new StringBuffer("a.name ,b.article_id FROM article_tag a,article_and_tag_relation b WHERE a.id = b.tag_id AND b.article_id IN ");
                stringBuffer.append("(");
                id.stream().forEach(p -> {
                    stringBuffer.append(p);
                    stringBuffer.append(",");
                });
                stringBuffer.delete(stringBuffer.lastIndexOf(","), stringBuffer.lastIndexOf(",") + 1);
                stringBuffer.append(")");
                SELECT(stringBuffer.toString());

            }
        }.toString();
    }



}
