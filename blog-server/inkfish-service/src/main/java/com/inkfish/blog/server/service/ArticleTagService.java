package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.server.mapper.ArticleTagMapper;
import com.inkfish.blog.server.model.dto.TagAndArticleDTO;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleComment;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HALOXIAO
 **/


@Service
public class ArticleTagService {

    @Autowired
    ArticleTagMapper articleTagMapper;

    public boolean addTags(List<ArticleTag> tags) {
        return articleTagMapper.saveBatch(tags);
    }

    public boolean deleteTagWithName(String name) {
        return articleTagMapper.remove(new QueryWrapper<ArticleTag>().eq("name", name));
    }

    public List<ArticleTag> getAllTags() {
        return articleTagMapper.list(new QueryWrapper<ArticleTag>().select("name"));
    }

    public List<Article> getArticle(String tag) {
        return articleTagMapper.getBaseMapper().getArticleOverview(tag);
    }

//    TODO need to change and fix bug of parallelStream
    public List<ArticleOverviewVO> setTagsForArticleOverviewVO(List<ArticleOverviewVO> list) {
        List<Integer> articlesId = new ArrayList<>(list.size());
        for (ArticleOverviewVO vo : list) {
            articlesId.add(vo.getId());
        }
        List<TagAndArticleDTO> result = articleTagMapper.getBaseMapper().getTagAndArticleDTO(articlesId);
        Collections.sort(result);
        TagAndArticleDTO dto = new TagAndArticleDTO();
        list.parallelStream().forEach(p -> {
            dto.setArticleId(p.getId());
            ThreadLocal<Integer> index = new ThreadLocal<>();
            index.set(Collections.binarySearch(result, dto));
            ThreadLocal<Integer> base = index;
            List<String> tags = new LinkedList<>();
//            获取排序好的tagAndArticleDTOList里index周围的符合条件的tag
            if (result.size() != 0) {

                while (base.get() + 1 != result.size() && result.get(base.get() + 1) != null && result.get(base.get() + 1).getArticleId().equals(
                        dto.getArticleId())) {
                    tags.add(result.get(base.get() + 1).getNames());
                    base.set(base.get() + 1);
                }
                base = index;
                while (-1 != base.get() - 1 && result.get(base.get() - 1) != null && result.get(base.get() - 1).getArticleId().equals(
                        dto.getArticleId())) {
                    tags.add(result.get(base.get() - 1).getNames());
                    base.set(base.get() - 1);
                }
            }
            p.setTags(tags);
        });

        return list;
    }



}
