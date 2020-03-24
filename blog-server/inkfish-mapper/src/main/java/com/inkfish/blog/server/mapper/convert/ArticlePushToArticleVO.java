package com.inkfish.blog.server.mapper.convert;

import com.inkfish.blog.server.model.front.ArticlePush;
import com.inkfish.blog.server.model.vo.ArticleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface ArticlePushToArticleVO {

    ArticlePushToArticleVO INSTANCE = Mappers.getMapper(ArticlePushToArticleVO.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "enableComment", target = "enableComment"),
            @Mapping(target = "tags", ignore = true),
            @Mapping(target = "vote", ignore = true),
            @Mapping(target = "watch", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    ArticleVO from(ArticlePush articlePush);

}
