package com.inkfish.blog.server.mapper.convert;

import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.vo.ArticleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author HALOXIAO
 **/

@Mapper
public interface ArticleToArticleVO {
    ArticleToArticleVO INSTANCE = Mappers.getMapper(ArticleToArticleVO.class);

    @Mappings({
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "content", source = "content"),
            @Mapping(target = "createTime", source = "createTime"),
            @Mapping(target = "updateTime", source = "updateTime"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "vote", ignore = true),
            @Mapping(target = "watch", ignore = true),
            @Mapping(target = "tags", ignore = true)

    })
    ArticleVO toArticleVO(Article article);

}
