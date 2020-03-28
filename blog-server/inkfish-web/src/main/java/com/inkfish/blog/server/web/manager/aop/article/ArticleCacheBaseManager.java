package com.inkfish.blog.server.web.manager.aop.article;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.inkfish.blog.server.common.*;
import com.inkfish.blog.server.mapper.convert.ArticlePushToArticleOverviewVO;
import com.inkfish.blog.server.model.front.ArticlePush;
import com.inkfish.blog.server.model.vo.ArticleHomeVO;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.model.vo.ArticleVO;
import com.inkfish.blog.server.service.ArticleService;
import com.inkfish.blog.server.service.ArticleTagService;
import com.inkfish.blog.server.service.UserBehaviorService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author HALOXIAO
 **/
@Component
@Aspect
@Order(10)
public class ArticleCacheBaseManager {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserBehaviorService userBehaviorService;
    private final ArticleTagService articleTagService;
    private final ArticleService articleService;

    protected final Duration ARTICLE_EXPIRE_TIME = Duration.ofDays(1);

    protected final String DATE_PATTERN = "yyyy-MM-dd";


    @Autowired
    public ArticleCacheBaseManager(StringRedisTemplate stringRedisTemplate, UserBehaviorService userBehaviorService, ArticleTagService articleTagService, ArticleService articleService) {
        this.articleTagService = articleTagService;
        this.userBehaviorService = userBehaviorService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.articleService = articleService;
    }


    /**
     * 返回Article的Cache
     */
    @Before("execution(* com.inkfish.blog.server.web.controller.ArticleController.getArticle(Integer))&&args(id)")
    public void getArticleCache(Integer id) throws IOException {
        String content = stringRedisTemplate.opsForValue().get(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_INFORMATION_PREFIX.getValue() + id);
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        if (null != content && null != response) {
            response.setCharacterEncoding("utf-8");
            response.setStatus(200);
            response.setContentType("application/json; charset=UTF-8");
            ResultBean<ArticleVO> bean = JSON.parseObject(content, new TypeReference<ResultBean<ArticleVO>>() {
            });
//            获得动态的Likes和Views
            List<Object> result = stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.zScore(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue().getBytes(), id.toString().getBytes());
                    connection.zScore(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue().getBytes(), id.toString().getBytes());
                    return null;
                }
            });
            Double like = (Double) result.get(0);
            Double view = (Double) result.get(1);
            if (like != null) {
                bean.getData().setVote(like.intValue());
            }
            if (view != null) {
                bean.getData().setWatch(view.intValue());
            }
            response.setContentType("application/json; charset=UTF-8");
            try (OutputStream stream = response.getOutputStream()) {
                stream.write(JSON.toJSON(bean).toString().getBytes());
                stream.flush();
            }
        }

    }

    /**
     * 更新缓存
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.getArticle(Integer))&&args(id)", returning = "bean", argNames = "id,bean")
    public void updateArticleCache(Integer id, ResultBean<ArticleVO> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            stringRedisTemplate.opsForValue().set(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_INFORMATION_PREFIX.getValue(), JSON.toJSON(bean).toString(), ARTICLE_EXPIRE_TIME);
        }
    }

    /**
     * 返回预览页的缓存
     */
    @Before(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.getHome(Integer,Integer)) &&args(page,size)", argNames = "page,size")
    public void getHomeCache(Integer page, Integer size) throws IOException {
        int num = page * size;
        final int articleHomeListLength = 2;
        List<Object> articleHomeList = stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.zRevRange(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue().getBytes(), num - size, num - 1);
                connection.get(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_COUNT.getValue().getBytes());
                return null;
            }
        });
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        if (articleHomeList.size() == articleHomeListLength && null != response) {
            Set<String> articleVOList = (Set<String>) articleHomeList.get(0);
            Integer articleCount = (Integer) articleHomeList.get(1);
            List<ArticleOverviewVO> list = new LinkedList<>();
            articleVOList.forEach(content -> {
                list.add(JSON.parseObject(content, ArticleOverviewVO.class));
            });
            ResultBean<ArticleHomeVO> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            ArticleHomeVO result = new ArticleHomeVO();
            result.setArticleList(list);
            result.setArticlesTotal(articleCount);
            bean.setData(result);
            response.setStatus(200);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=UTF-8");
            try (ServletOutputStream stream = response.getOutputStream()) {
                stream.write(JSON.toJSON(bean).toString().getBytes());
                stream.flush();
            }
        }
    }

    /**
     * 如果getHome成功，则更新Article Home Cache
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.getHome(Integer,Integer))&&args(page,size)", returning = "bean", argNames = "page,size,bean")
    public void addHomeCache(Integer page, Integer size, ResultBean<ArticleHomeVO> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
            bean.getData().getArticleList().forEach(articleOverviewVO -> {
                set.add(new DefaultTypedTuple<>((String) JSON.toJSON(articleOverviewVO), articleOverviewVO.getId().doubleValue()));
            });
            stringRedisTemplate.opsForZSet().add(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), set);
        }
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.deleteArticle(Integer)) &&args(id)", returning = "bean", argNames = "id,bean")
    public void deleteHomeCache(Integer id, ResultBean<String> bean) {
        if (bean.getCode() == RESULT_BEAN_STATUS_CODE.SUCCESS.getValue()) {
            stringRedisTemplate.opsForZSet().remove(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), id);
        }
    }


    /**
     * 更新HomeCache的Article
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.publishArticle())&&args(articleP)", returning = "bean", argNames = "articleP,bean")
    public void updateHomeCache(ArticlePush articleP, ResultBean<Integer> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            Integer id = bean.getData();
            articleP.setId(id);
            ArticleOverviewVO overviewVO = ArticlePushToArticleOverviewVO.INSTANCE.from(articleP);
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
            if (articleP.getId() == null) {
//                发布文章
                overviewVO.setUpdateTime(now);
                overviewVO.setCreateTime(now);
                overviewVO.setLikes(0);
                overviewVO.setViews(0);
                overviewVO.setId(bean.getData());
                stringRedisTemplate.opsForZSet().add(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), JSON.toJSON(overviewVO).toString(), id.doubleValue());
            } else {
//                更新文章
                Map<VOTE_LIKES, Integer> map = userBehaviorService.getArticleLikesAndViewsCache(id);
                DateFormat format = new SimpleDateFormat(DATE_PATTERN);
                Timestamp createTime = articleService.getCreateTime(bean.getData());
                overviewVO.setViews(map.get(VOTE_LIKES.WATCH.getValue()));
                overviewVO.setLikes(map.get(VOTE_LIKES.VOTE.getValue()));
                overviewVO.setUpdateTime(now);
                overviewVO.setTags(articleP.getTagsName());
                overviewVO.setCreateTime(format.format(createTime));
                stringRedisTemplate.opsForZSet().add(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), JSON.toJSON(overviewVO).toString(), id.doubleValue());
            }
        }
    }

    /**
     * 删除Article Cache
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.publishArticle())&&args(articleP,bindingResult)", returning = "bean", argNames = "articleP,bindingResult,bean")
    public void deleteArticleCache(ArticlePush articleP, BindingResult bindingResult, ResultBean<Integer> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode() && null != articleP.getId()) {
            if (null != articleP.getId()) {
                stringRedisTemplate.delete(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_INFORMATION_PREFIX.getValue() + articleP.getId());
            }
        }
    }

    /**
     * 更新Article Count
     */
    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.publishArticle())&&args(articleP,bindingResult)", returning = "bean", argNames = "articleP,bindingResult,bean")
    public void updateArticleCount(ArticlePush articleP, BindingResult bindingResult, ResultBean<Integer> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode() && null == articleP.getId()) {
            stringRedisTemplate.opsForValue().increment(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_COUNT.getValue(), 1);
        }
    }


}
