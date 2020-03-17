package com.inkfish.blog.server.web.controller;

import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.vo.PanelLikesVO;
import com.inkfish.blog.server.model.vo.PanelViewsVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author HALOXIAO
 **/
@Api("管理员信息相关")
@RestController
public class AdminPanelController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/admin/information/likes")
    public ResultBean<List<PanelLikesVO>> getLikes() {
        Set<ZSetOperations.TypedTuple<String>> likes = stringRedisTemplate.opsForZSet().rangeByScoreWithScores(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue(), -10, -1);
        if (likes == null) {
            return new ResultBean<>("some thing fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        ArrayList<PanelLikesVO> result = new ArrayList<>(likes.size());
        likes.forEach(p -> {
            PanelLikesVO panelLikesVO = new PanelLikesVO();
            if (p.getScore() == null) {
                panelLikesVO.setLikes(0);
            } else {
                panelLikesVO.setLikes(p.getScore().intValue());
            }
            panelLikesVO.setTitle(p.getValue());
            result.add(panelLikesVO);
        });
        ResultBean<List<PanelLikesVO>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(result);
        return bean;
    }

    @GetMapping("/admin/information/views")
    public ResultBean<List<PanelViewsVO>> getViews() {
        Set<ZSetOperations.TypedTuple<String>> views = stringRedisTemplate.opsForZSet().rangeByScoreWithScores(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue(), -10, -1);
        if (views == null) {
            return new ResultBean<>("some thing fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        ArrayList<PanelViewsVO> result = new ArrayList<>(views.size());
        views.forEach(p -> {
            PanelViewsVO panelViewsVO = new PanelViewsVO();
            if (p.getScore() == null) {
                panelViewsVO.setViews(0);
            } else {
                panelViewsVO.setViews(p.getScore().intValue());
            }
            panelViewsVO.setTitle(p.getValue());
            result.add(panelViewsVO);
        });
        ResultBean<List<PanelViewsVO>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(result);
        return bean;
    }


}
