package com.inkfish.blog.server.mapper.convert;


import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface ArticleToArticleOverviewVO {

    ArticleToArticleOverviewVO INSTANCE = Mappers.getMapper(ArticleToArticleOverviewVO.class);


    List<ArticleOverviewVO> toArticleOverviewVOList(List<Article> article);


    @Mappings({
            @Mapping(source = "overview", target = "overview"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "updateTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "tags", ignore = true),
            @Mapping(target = "likes", ignore = true),
            @Mapping(target = "views", ignore = true)
    })
    ArticleOverviewVO toArticle(Article article);
}
