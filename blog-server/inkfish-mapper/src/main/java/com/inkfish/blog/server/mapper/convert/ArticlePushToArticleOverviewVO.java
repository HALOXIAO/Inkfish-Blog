package com.inkfish.blog.server.mapper.convert;

import com.inkfish.blog.server.model.front.ArticlePush;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface ArticlePushToArticleOverviewVO {

    ArticlePushToArticleOverviewVO INSTANCE = Mappers.getMapper(ArticlePushToArticleOverviewVO.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(target = "overview", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "tags", ignore = true),
            @Mapping(target = "likes", ignore = true),
            @Mapping(target = "views", ignore = true)
    })
    ArticleOverviewVO from(ArticlePush articlePush);
}
