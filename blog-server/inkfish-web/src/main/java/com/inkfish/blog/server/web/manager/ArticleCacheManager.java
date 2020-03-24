package com.inkfish.blog.server.web.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.inkfish.blog.server.common.*;
import com.inkfish.blog.server.mapper.convert.ArticlePushToArticleVO;
import com.inkfish.blog.server.model.front.ArticlePush;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.model.vo.ArticleVO;
import com.inkfish.blog.server.service.UserBehaviorService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author HALOXIAO
 **/
@Order(1)
@Component
@Aspect
public class ArticleCacheManager {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserBehaviorService userBehaviorService;

    protected final Duration ARTICLE_EXPIRE_TIME = Duration.ofDays(1);

    protected final String DATE_PATTERN = "yyyy-MM-dd";


    @Autowired
    public ArticleCacheManager(StringRedisTemplate stringRedisTemplate, UserBehaviorService userBehaviorService) {
        this.userBehaviorService = userBehaviorService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Before("execution(* com.inkfish.blog.server.web.controller.ArticleController.getArticle(Integer))&&args(id)")
    public void getArticleCache(Integer id) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        String content = stringRedisTemplate.opsForValue().get(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_INFORMATION_NAMESPACE.getValue() + id);
        if (null != content) {
            response.setCharacterEncoding("utf-8");
            response.setStatus(200);
            response.setHeader("Content-Type", "application/json");
            ResultBean<ArticleVO> bean = JSON.parseObject(content, new TypeReference<ResultBean<ArticleVO>>() {
            });
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
            try (OutputStream stream = response.getOutputStream()) {
                stream.write(JSON.toJSON(content).toString().getBytes());
                stream.flush();
            }
        }

    }


    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.getArticle(Integer))&&args(id)", returning = "bean", argNames = "id,bean")
    public void updateArticleCache(Integer id, ResultBean<ArticleVO> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            stringRedisTemplate.opsForValue().set(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_INFORMATION_NAMESPACE.getValue(), JSON.toJSON(bean).toString(), ARTICLE_EXPIRE_TIME);
        }
    }

    @Before("execution(* com.inkfish.blog.server.web.controller.ArticleController.getHome(Integer)) &&args(id)")
    public void getHomeCache(Integer id) throws IOException {
        Integer num = id * 10;
        Set<String> result = stringRedisTemplate.opsForZSet().reverseRange(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), num - 10, num - 1);
        if (null != result) {
            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
            List<ArticleOverviewVO> list = new LinkedList<>();
            result.forEach(content -> {
                list.add(JSON.parseObject(content, ArticleOverviewVO.class));
            });
            ResultBean<List<ArticleOverviewVO>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            bean.setData(list);
            response.setStatus(200);
            response.setCharacterEncoding("utf-8");
            try (PrintWriter printWriter = response.getWriter()) {
                printWriter.write(JSON.toJSON(bean).toString());
                printWriter.flush();
            }
        }
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.getHome(Integer)) &&args(id)", returning = "bean", argNames = "id,bean")
    public void updateHomeCache(Integer id, ResultBean<List<ArticleOverviewVO>> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            ConcurrentSkipListSet<ZSetOperations.TypedTuple<String>> set = new ConcurrentSkipListSet<>();
            bean.getData().parallelStream().forEach(articleOverviewVO -> {
                set.add(new DefaultTypedTuple<>((String) JSON.toJSON(articleOverviewVO), articleOverviewVO.getId().doubleValue()));
            });
            stringRedisTemplate.opsForZSet().add(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), set);
        }
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.deleteArticle(Integer)) &&args(id)", returning = "bean", argNames = "id,bean")
    public void deleteHomeCache(Integer id, ResultBean<String> bean) {
        if (bean.getCode() == RESULT_BEAN_STATUS_CODE.SUCCESS.getValue()) {
            stringRedisTemplate.opsForZSet().removeRangeByScore(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), id, id);
        }
    }


    @Pointcut("execution(* com.inkfish.blog.server.web.controller.ArticleController.publishArticle(articleP)) &&args(articleP)")
    public void publishArticle(ArticlePush articleP) {}

//TODO EnableComment、草稿 判断
    @AfterReturning(value = "publishArticle(articleP)", returning = "bean", argNames = "articleP,bean")
    public void changeHomeCache(ArticlePush articleP, ResultBean<Integer> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
//        发布文章
            if (null == articleP.getId()) {
                Integer id = bean.getData();
                //TODO ArticleOverviewVO
                ArticleVO article = ArticlePushToArticleVO.INSTANCE.from(articleP);
                article.setId(id);
                article.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
                article.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
                Map<VOTE_LIKES, Integer> map = userBehaviorService.getArticleLikesAndViewsById(id);
                article.setWatch(map.get(VOTE_LIKES.WATCH.getValue()));
                article.setVote(map.get(VOTE_LIKES.VOTE.getValue()));
                ResultBean<ArticleVO> resultBean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
                resultBean.setData(article);
                stringRedisTemplate.opsForZSet().add(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_HOME_OVERVIEW.getValue(), JSON.toJSON(resultBean).toString(), id.doubleValue());
           }  else {
//        更新文章


            }

        }
    }

    @AfterReturning(value = "publishArticle(articleP)", returning = "bean", argNames = "articleP,bean")
    public void deleteArticleCache(ArticlePush articleP, ResultBean<Integer> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            if (null != articleP.getId()) {
                stringRedisTemplate.delete(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_INFORMATION_NAMESPACE.getValue() + articleP.getId());
            }
        }
    }
}
