package com.inkfish.blog.server.service.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.server.common.REDIS_TAG_CACHE_NAMESPACE;
import com.inkfish.blog.server.mapper.ArticleTagMapper;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.service.ArticleTagService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HALOXIAO
 **/
@Component
public class ArticleServiceUtils {

    private final StringRedisTemplate stringRedisTemplate;
    private final ArticleTagMapper articleTagMapper;

    public ArticleServiceUtils(StringRedisTemplate stringRedisTemplate, ArticleTagMapper articleTagMapper) {
        this.articleTagMapper = articleTagMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 判断tags是否在cache中
     */
    public List<String> checkTagsIsMemberInCache(List<ArticleTag> tags) {
        stringRedisTemplate.multi();
        tags.forEach(tag -> {
                    stringRedisTemplate.opsForSet().isMember(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG.getValue(), tag.getName());
                }
        );
        List<Object> result = stringRedisTemplate.exec();

        if (result.isEmpty()) {
            return null;
        } else {
            List<String> newTags = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                Object tag = result.get(i);
                if (!((boolean) tag)) {
                    newTags.add(tags.get(i).getName());
                }
            }
            return newTags;
        }
    }

    /**
     * 判断tags是否在DB中
     */
    public List<String> checkTagsIsMemberInDB(List<ArticleTag> tags) {
        List<ArticleTag> list = articleTagMapper.list(new QueryWrapper<ArticleTag>().select("name"));
        List<String> tagNames = list.stream().map(ArticleTag::getName).collect(Collectors.toList());
        HashSet<String> set = new HashSet<>((int) ((list.size() / 0.75f) + 1));
        set.addAll(tagNames);
        List<String> exTags = new ArrayList<>();
        tagNames.forEach(name -> {
            if (set.add(name)) {
                exTags.add(name);
            }
        });
        return exTags;
    }


}
