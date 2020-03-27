package com.inkfish.blog.server.web.manager.aop.tag;

import com.alibaba.fastjson.JSON;
import com.inkfish.blog.server.common.REDIS_TAG_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.vo.ArticleTagVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author HALOXIAO
 **/

@Aspect
@Component
public class ArticleTagCacheBaseManager {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public ArticleTagCacheBaseManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.TagController.allTags(Integer,Integer))&&args(page,size)", returning = "bean", argNames = "page,size,bean")
    public void addArticleTagCache(Integer page, Integer size, ResultBean<List<ArticleTagVO>> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    bean.getData().stream().forEach(articleTagVO -> {
                                connection.zAdd(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG_HOME.getValue().getBytes(), articleTagVO.getId().doubleValue(), JSON.toJSON(articleTagVO).toString().getBytes());
                            }
                    );

                    return null;
                }
            });
        }
    }

    @Before(value = "execution(* com.inkfish.blog.server.web.controller.TagController.allTags(Integer,Integer))&&args(page,size)", argNames = "page,size")
    public void getArticleTagCache(Integer page, Integer size) throws IOException {

        Set<String> result = stringRedisTemplate.opsForZSet().reverseRange(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG_HOME.getValue(), page - size, page - 1);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (null != response && null != result) {
            try (PrintWriter printWriter = response.getWriter()) {
//                TODO 改一下write
                printWriter.write(result.toString());
                printWriter.flush();
            }
        }
    }


}
