package com.inkfish.blog.server.web.controller;

import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.pojo.CommentReply;
import com.inkfish.blog.server.service.CommentReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author HALOXIAO
 **/

@RestController
@Slf4j
public class ReplyCommentController {

    @Autowired
    CommentReplyService commentReplyService;

    @GetMapping("/article/reply")
    public ResultBean<List<CommentReply>> getCommentReplyPage(Integer commentId, Integer page, Integer size) {
        if (page < 0 || size < 0) {
            return new ResultBean<>("argument error", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        ResultBean<List<CommentReply>> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        bean.setData(commentReplyService.getCommentReplyPage(commentId, page, size));
        return bean;
    }

    @PostMapping("/article/reply")
    public ResultBean<Boolean> pushCommentReply(@RequestBody @Valid CommentReply commentReply, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResultBean<Boolean> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            if (bindingResult.getFieldError() != null) {
                log.warn(bindingResult.getFieldError().getField());
            }
        }
            if (commentReplyService.pushCommentReply(commentReply)) {
                return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            }
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);

    }

    @DeleteMapping("/article/reply")
    public ResultBean<Boolean> deleteCommentReply(Integer id) {
        if (commentReplyService.deleteCommentReply(id)) {
            return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        }
        return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
    }

}
