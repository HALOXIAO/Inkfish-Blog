package com.inkfish.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.mapper.ImageMapper;
import com.inkfish.blog.model.pojo.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@Service
public class ImageService {

    @Autowired
    ImageMapper imageMapper;

    private static final String imageDirName = "image";

    public String addImage(MultipartFile file) throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");//设置日期格式
        String name = LocalDateTime.now().format(fmt);
        name = name + "." + file.getContentType();
        File storeFile = new File("../../" + imageDirName);
        if (!storeFile.exists()) {
            storeFile.mkdir();
        }
        file.transferTo(storeFile);
        return name;
    }

    public Boolean addImageUrls(List<Image> urls) {
        return imageMapper.saveBatch(urls);
    }

    public Boolean updateImageArticleId(Integer articleId,String username){
        Image image = new Image();
        image.setArticleId(articleId);
        return imageMapper.update(image,new QueryWrapper<Image>().eq("username",username));
    }

}


