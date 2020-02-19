package com.inkfish.blog.web.controller;

import com.inkfish.blog.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.common.ResultBean;
import com.inkfish.blog.mapper.convert.ArticlePushToArticle;
import com.inkfish.blog.model.front.ArticlePush;
import com.inkfish.blog.model.pojo.Article;
import com.inkfish.blog.service.ArticleService;
import com.inkfish.blog.service.CategoryService;
import com.inkfish.blog.service.manager.ImageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        ResultBean<Integer> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(article.getId());
        return bean;
    }

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


    @PostMapping("/articleImage")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    public ResultBean<List<String>> uploadImage(@RequestParam("file") List<MultipartFile> files, String title, Integer id) {
        List<String> path = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                path.add(imageManager.addImage(file, title, id));
            } catch (IOException e) {
                log.error(e.getMessage());
                return new ResultBean<>("upload image fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
            }
        }
        ResultBean<List<String>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(path);
        return bean;
    }

}
