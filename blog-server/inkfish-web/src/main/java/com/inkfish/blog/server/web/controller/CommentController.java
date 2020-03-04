package com.inkfish.blog.server.web.controller;

import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.pojo.ArticleComment;
import com.inkfish.blog.server.service.CommentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@Api("文章评论相关操作")
@RestController
@Slf4j
public class CommentController {

    @Autowired
    CommentService commentService;


    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @GetMapping("/article/comment")
    public ResultBean<List<ArticleComment>> getAllComment(Integer articleId, Integer page, Integer size) {
        if (page == null || size ==null || page <= 0 || size < 0) {
            return new ResultBean<>("bad argument", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        page--;
        ResultBean<List<ArticleComment>> lists = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        lists.setData(commentService.getCommentWithPage(articleId, page, size));
        return lists;
    }

    @PostMapping("/article/comment")
    public ResultBean<Boolean> pushComment(@RequestBody @Valid ArticleComment articleComment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResultBean<Boolean> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            if (bindingResult.getFieldError() != null) {
                log.warn(bindingResult.getFieldError().getField());
            }
            return bean;
        }
        articleComment.setCommentCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        if (commentService.addComment(articleComment)) {
            return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        }

        return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
    }

    @DeleteMapping("article/comment")
    public ResultBean<Boolean> deleteComment(Integer id) {
        if (commentService.deleteComment(id)) {
            return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        }
        return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);

    }


}
