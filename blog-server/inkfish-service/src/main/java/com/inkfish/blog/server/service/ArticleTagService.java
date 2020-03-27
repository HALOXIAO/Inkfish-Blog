package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author HALOXIAO
 **/


@Service
public class ArticleTagService {

    @Autowired
    ArticleTagMapper articleTagMapper;

    @Autowired
    PaginationInterceptor iPage;

    public boolean addTags(List<ArticleTag> tags) {
        return articleTagMapper.saveBatch(tags);
    }

    public boolean deleteTagWithName(String name) {
        return articleTagMapper.remove(new QueryWrapper<ArticleTag>().eq("name", name));
    }

    public List<String> getTagsNameByArticleId(Integer id) {
        List<ArticleTag> list = articleTagMapper.getBaseMapper().getTagsNameByArticleId(id);
        if (list == null) {
            return null;
        }
        ArrayList<String> result = new ArrayList<>(list.size());
        list.forEach(tag -> {
            result.add(tag.getName());
        });
        return result;
    }


    public IPage<ArticleTag> getTagsNameWithPage(Integer page) {
        Page<ArticleTag> ipage = new Page<>(page, 10);
        return articleTagMapper.page(ipage, new QueryWrapper<ArticleTag>().select("name", "id").eq("status", 1));
    }

    public List<Article> getArticle(String tag) {
        return articleTagMapper.getBaseMapper().getArticleOverview(tag);
    }

    public List<ArticleOverviewVO> setTagsForArticleOverviewVO(List<ArticleOverviewVO> list) {
        List<Integer> articlesId = new ArrayList<>(list.size());
        for (ArticleOverviewVO vo : list) {
            articlesId.add(vo.getId());
        }
//        只是Get操作，不会有并发安全性
        List<TagAndArticleDTO> result = articleTagMapper.getBaseMapper().getTagAndArticleDTO(articlesId);
        Collections.sort(result);
        list.stream().forEach(p -> {
            TagAndArticleDTO dto = new TagAndArticleDTO();
            dto.setArticleId(p.getId());

            int index = Collections.binarySearch(result, dto);
            int base = index;
            List<String> tags = new ArrayList<>();
//            获取排序好的tagAndArticleDTOList里index周围的符合条件的tag
            if (result.size() != 0) {
                while (base + 1 != result.size() && result.get(base + 1) != null && result.get(base + 1).getArticleId().equals(
                        dto.getArticleId())) {
                    tags.add(result.get(base + 1).getNames());
                    base = base + 1;
                }
                base = index;
                while (-1 != base - 1 && result.get(base - 1) != null && result.get(base - 1).getArticleId().equals(
                        dto.getArticleId())) {
                    tags.add(result.get(base - 1).getNames());
                    base = base - 1;
                }
            }
            p.setTags(tags);
        });

        return list;
    }

    public boolean deleteArticleTag(Integer tagId) {
        return articleTagMapper.remove(new QueryWrapper<ArticleTag>().eq("id", tagId));
    }


    public boolean saveOrUpdateArticleTags(Integer articleId, List<ArticleTag> tagsName) {

        return false;
    }

}
