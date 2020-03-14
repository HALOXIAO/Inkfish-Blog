package com.inkfish.blog.server.web.controller;

import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.mapper.convert.ArticlePushToArticle;
import com.inkfish.blog.server.model.front.ArticlePush;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.service.ArticleService;
import com.inkfish.blog.server.service.CategoryService;
import com.inkfish.blog.server.service.manager.ImageManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author HALOXIAO
 **/
@Api("文章操作")
@RestController
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @ApiOperation(value = "获取文章")
    @ApiResponse(code = 200, message = "返回文章实体的所有信息")
    @GetMapping("/article")
    public ResultBean<Article> getArticle(@RequestParam(value = "id") Integer id) {
        if (id == null) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        Article article = articleService.getArticle(id);
        article.setId(id);
        ResultBean<Article> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(article);
        return bean;
    }

    @ApiOperation(value = "给文章进行点赞")
    @ApiResponse(code = 200, message = "返回的data为点赞数")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_NORMAL')")
    @GetMapping("/article/like")
    public ResultBean<Integer> articleLike(Integer id) {
        Integer like = (Integer) redisTemplate.opsForHash().get(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue(), String.valueOf(id));
        ResultBean<Integer> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(like + 1);
        return bean;
    }

    @ApiOperation(value = "发布或更新文章")
    @ApiResponse(code = 200, message = "有可能返回的Code：参数异常、成功、未知异常、未登录、无权限")
    @PostMapping("/article")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    public ResultBean<Integer> publishArticle(@RequestBody @Valid ArticlePush articleP, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResultBean<Integer> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            if (bindingResult.getFieldError() != null) {
                log.warn(bindingResult.getFieldError().getField());
            }
            return bean;
        }
        Article article = ArticlePushToArticle.INSTANCE.from(articleP);
        Integer categoryId = categoryService.searchIdWithName(articleP.getCategoryName());
        article.setCategoryId(categoryId);
        article.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        if (!articleService.addArticle(article)) {
            log.warn("add article fail");
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        ResultBean<Integer> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(article.getId());
        return bean;
    }

    @ApiResponse(code = 200, message = "有可能返回的Code：成功、未知异常、未登录、无权限")
    @DeleteMapping("/article")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    public ResultBean<String> deleteArticle(Integer id) {
        try {
            articleService.deleteArticleById(id);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);

    }


    @ApiOperation(value = "上传图片", notes = "第一次上传时，id可以为空，返回为图片的地址，图片的大小暂时限定在6MB以内")
    @ApiResponse(code = 200, message = "有可能返回的Code：成功、未知异常、无权限、未登录")
    @PostMapping("/articleImage")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    public ResultBean<List<String>> uploadImage(@RequestParam("file") List<MultipartFile> files, String title, Integer id) {
        List<String> paths = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        //注意，需要顺序处理
        for (MultipartFile file : files) {
            try {
                String path = imageManager.addImage(file, title, id, localDateTime);
                if (null == path) {
                    return new ResultBean<>("title already exist", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
                }
                paths.add(path);


            } catch (IOException e) {
                log.error(e.getMessage());
                return new ResultBean<>("upload image fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
            }
        }
        ResultBean<List<String>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(paths);
        return bean;
    }


    @ApiOperation(value = "首页信息", notes = "page为当前页数，最小为1，size为容量，最小为0")
    @ApiResponse(code = 200, message = "有可能返回的Code：参数异常、成功、未知异常、未登录、")
    @GetMapping("/home")
    public ResultBean<List<ArticleOverviewVO>> getHome(Integer page, Integer size) {
        if (page == null || size == null || page <= 0 || size < 0) {
            return new ResultBean<>("argument error", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        page--;
        List<ArticleOverviewVO> list = articleService.getArticleOverviewPage(page, size);
        ResultBean<List<ArticleOverviewVO>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(list);
        return bean;
    }




/*
* SELECT name FROM article_tag WHERE id INNER IN (
SELECT tag_id FROM article_and_tag_relation WHERE article_id INNER IN(
SELECT id FROM article WHERE id = 14 OR id=15
	)
)
* */

}
