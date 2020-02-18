package com.inkfish.blog.web.controller;

import com.inkfish.blog.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.common.ResultBean;
import com.inkfish.blog.common.exception.DBTransactionalException;
import com.inkfish.blog.mapper.convert.ArticlePushToArticle;
import com.inkfish.blog.model.front.ArticlePush;
import com.inkfish.blog.model.pojo.Article;
import com.inkfish.blog.model.pojo.Image;
import com.inkfish.blog.service.ArticleService;
import com.inkfish.blog.service.CategoryService;
import com.inkfish.blog.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    ArticleService articleService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ImageService imageService;

    @Autowired
    HttpSession httpSession;

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
        if (!articleService.addArticle(article)&& !imageService.updateImageArticleId(article.getId(), (String) httpSession.getAttribute("username"))) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        ResultBean<Integer> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(article.getId());
        return bean;
    }


    //TODO 修改图片服务器
    @PostMapping("/articleImage")
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @Transactional(rollbackFor = DBTransactionalException.class)
    public ResultBean<List<String>> uploadImage(@RequestParam("file") List<MultipartFile> files) {
        List<String> path = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                path.add(imageService.addImage(file));
            } catch (IOException e) {
                log.error(e.getMessage());
                return new ResultBean<>("upload image fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
            }
        }
        String username = (String) httpSession.getAttribute("username");
        List<Image> images = new ArrayList<>();
        path.parallelStream().forEach(p -> {
            Image image = new Image();
            image.setImageUrl(p);
            image.setUsername(username);
            images.add(image);
        });
        if (!imageService.addImageUrls(images)) {
            return new ResultBean<>("upload image urls fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        ResultBean<List<String>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(path);
        return bean;
    }

}
