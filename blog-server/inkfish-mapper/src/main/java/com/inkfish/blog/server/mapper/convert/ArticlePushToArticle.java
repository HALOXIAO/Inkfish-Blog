package com.inkfish.blog.server.mapper.convert;

import com.inkfish.blog.server.model.front.ArticlePush;
import com.inkfish.blog.server.model.pojo.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface ArticlePushToArticle {

    ArticlePushToArticle INSTANCE = Mappers.getMapper(ArticlePushToArticle.class);

    @Mappings(
            {
                    @Mapping(source = "title", target = "title"),
                    @Mapping(source = "enableComment", target = "enableComment"),
                    @Mapping(source = "overview", target = "overview"),
                    @Mapping(source = "status", target = "status"),
                    @Mapping(source = "content", target = "content")
            }
    )
    Article from(ArticlePush articlePush);

}
