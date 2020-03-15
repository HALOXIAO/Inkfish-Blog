package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.server.model.dto.TagAndArticleDTO;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author HALOXIAO
 **/

@Mapper
public interface ArticleTagDao extends BaseMapper<ArticleTag> {

    //TODO need to change
//    @Select("SELECT a.name ,b.article_id FROM article_tag a,article_and_tag_relation b WHERE\n" +"a.id = b.tag_id AND b.article_id IN(13,15)  ")
    @SelectProvider(type = ArticleTagMapper.class, method = "getTagAndArticleDTOProvider")
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "article_id", column = "articleId")
    })
    List<TagAndArticleDTO> getTagAndArticleDTO(@Param("idList")List<Integer> id);


    @Select("SELECT id,title,overview,create_time,update_time FROM article WHERE article_id IN (SELECT article_id FROM article_and_tag_relation WHERE tag_id = #{tag} )")
    List<Article> getArticleOverview(String tag);

}
