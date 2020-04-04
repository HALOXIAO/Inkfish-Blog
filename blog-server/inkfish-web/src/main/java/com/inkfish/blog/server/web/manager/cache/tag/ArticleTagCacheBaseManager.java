package com.inkfish.blog.server.web.manager.cache.tag;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.inkfish.blog.server.common.REDIS_TAG_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.common.exception.DBTransactionalException;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.model.vo.ArticleTagHomeVO;
import com.inkfish.blog.server.model.vo.ArticleTagVO;
import com.inkfish.blog.server.service.CountService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author HALOXIAO
 **/
@Order(9)
@Aspect
@Component
public class ArticleTagCacheBaseManager {

    private final StringRedisTemplate stringRedisTemplate;
    private final CountService countService;

    @Autowired
    public ArticleTagCacheBaseManager(StringRedisTemplate stringRedisTemplate, CountService countService) {
        this.countService = countService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 从DB获取TagHome后，更新Tag Home Cache
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.TagController.allTags(Integer,Integer))&&args(page,size)", returning = "bean", argNames = "page,size,bean")
    public void addArticleTagCache(Integer page, Integer size, ResultBean<List<ArticleTagVO>> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    bean.getData().forEach(articleTagVO -> {
                                connection.zAdd(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG_HOME.getValue().getBytes(), articleTagVO.getId().doubleValue(), JSON.toJSON(articleTagVO).toString().getBytes());
                            }
                    );
                    return null;
                }
            });
        }
    }

    /**
     * 当删除Tag成功后，从Redis中删除“已存在的Tag”Cache
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.TagController.deleteTag(Integer))&&args(id)", returning = "bean", argNames = "id,bean")
    public void deleteArticleTagCache(Integer id, ResultBean<Boolean> bean) {
        if (bean.getCode() == RESULT_BEAN_STATUS_CODE.SUCCESS.getValue()) {
            stringRedisTemplate.opsForSet().remove(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG.getValue(), id.toString());
        }

    }

    /**
     * 当删除Tag成功后，从Redis中的Tag Home删除指定的tag
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.TagController.deleteTag(Integer))&&args(id)", returning = "bean", argNames = "id,bean")
    public void deleteArticleTagHomeCache(Integer id, ResultBean<Boolean> bean) {
        if (bean.getCode() == RESULT_BEAN_STATUS_CODE.SUCCESS.getValue()) {
            stringRedisTemplate.opsForZSet().remove(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG_HOME.getValue(), id.toString());
        }
    }

    /**
     * 拦截获取TagHome的请求，从Redis返回Tag Home Cache
     */
    @Before(value = "execution(* com.inkfish.blog.server.web.controller.TagController.allTags(Integer,Integer))&&args(page,size)", argNames = "page,size")
    public void getArticleTagCache(Integer page, Integer size) throws IOException {

        Set<String> result = stringRedisTemplate.opsForZSet().reverseRange(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG_HOME.getValue(), page - size, page - 1);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (null != response && null != result) {
            List<ArticleTagVO> tags = new LinkedList<>();
            result.forEach(tag -> {
                tags.add(JSON.parseObject(tag, ArticleTagVO.class));
            });
            ArticleTagHomeVO tagHome = new ArticleTagHomeVO();
            tagHome.setTagsList(tags);
            tagHome.setTagTotal(countService.getTagCount());
            ResultBean<ArticleTagHomeVO> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            bean.setData(tagHome);
            response.setContentType("application/json; charset=UTF-8");
            try (ServletOutputStream stream = response.getOutputStream()) {
                stream.write(JSON.toJSON(bean).toString().getBytes());
                stream.flush();
            }
        }
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.TagController.addTags())&&args(tagsName)", returning = "bean", argNames = "tagsName,bean")
    public void updateTagsCache(List<String> tagsName, ResultBean<Boolean> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    tagsName.forEach(name -> {
                                connection.sAdd(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG.getValue().getBytes(), name.getBytes());
                            }
                    );
                    return null;
                }

            });
        }
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.service.ArticleTagService.addTags())&&args(tagsName)", returning = "tags", argNames = "tagsName,tags")
    public void updateTagsHomeCache(List<String> tagsName, List<ArticleTag> tags) {
        if (null != tags) {
            List<ArticleTagVO> result = Lists.transform(tags, new Function<ArticleTag, ArticleTagVO>() {
                @Nullable
                @Override
                public ArticleTagVO apply(@Nullable ArticleTag input) {
                    ArticleTagVO tagVO = new ArticleTagVO();
                    tagVO.setTagName(input.getName());
                    tagVO.setId(input.getId());
                    return tagVO;
                }
            });
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    result.forEach(tag -> {
                        connection.zAdd(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG_HOME.getValue().getBytes(), tag.getId().doubleValue(), JSON.toJSON(tag).toString().getBytes());
                    });
                    return null;
                }
            });

        }
    }

    @Transactional(rollbackFor = DBTransactionalException.class)
    @AfterReturning(value = "execution(* com.inkfish.blog.server.service.ArticleTagService.addTags())&&args(tagsName)", returning = "tagsList", argNames = "tagsName,tagsList")
    public void updateTagCount(List<String> tagsName, List<ArticleTag> tagsList) {
        if (tagsList != null) {
            countService.addTagCount();
        }
    }

}
