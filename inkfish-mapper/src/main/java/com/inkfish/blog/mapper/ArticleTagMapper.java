package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.ArticleTag;
import org.springframework.stereotype.Component;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Component
public class ArticleTagMapper extends ServiceImpl<ArticleTagDao,ArticleTag> {


    public String getTagAndArticleDTOProvider(final List<Integer> id){
        return new SQL(){
            {
                StringBuffer stringBuffer = new StringBuffer("a.name ,b.article_id FROM article_tag a,article_and_tag_relation b WHERE a.id = b.tag_id AND b.article_id IN ");
                stringBuffer.append("(");
                id.parallelStream().forEach(p->{
                    stringBuffer.append(p);
                    stringBuffer.append(",");
                });
                stringBuffer.delete(stringBuffer.lastIndexOf(","),stringBuffer.lastIndexOf(",")+1);
                stringBuffer.append(")");
                SELECT(stringBuffer.toString());

            }
        }.toString();
    }

}
