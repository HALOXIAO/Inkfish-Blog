package com.inkfish.blog.server.web.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.inkfish.blog.server.common.REDIS_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.front.ArticlePush;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.model.vo.ArticleVO;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author HALOXIAO
 **/
@Order(1)
@Component
@Aspect
public class ArticleCacheManager {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public ArticleCacheManager(StringRedisTemplate stringRedisTemplate) {
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
            stringRedisTemplate.opsForValue().set(REDIS_CACHE_NAMESPACE.CACHE_ARTICLE_INFORMATION_NAMESPACE.getValue(), JSON.toJSON(bean).toString());
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

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.publishArticle()) &&args(articleP)", returning = "bean", argNames = "articleP,bean")
    public void changeHomeCache(ArticlePush articleP, ResultBean<String> bean) {

    }

}
