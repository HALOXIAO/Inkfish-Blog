package com.inkfish.blog.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.common.exception.CreateFileException;
import com.inkfish.blog.mapper.ArticleMapper;
import com.inkfish.blog.model.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author HALOXIAO
 **/
@Service
@Slf4j
public class ImageManager {


    @Autowired
    ArticleMapper articleMapper;

    private static final String imageDirName = "../../Image/";

    public String addImage(MultipartFile file, String title, Integer id) throws IOException {
        if (id != null) {
            deleteImage(id);
        }
        boolean flag = true;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");//设置日期格式
        String timeId = LocalDateTime.now().format(fmt);
        String code = Integer.toString(title.hashCode());
        StringBuffer buffer = new StringBuffer();
        buffer.append(imageDirName).append(code).append("-")
                .append(timeId);
        File storeFile = new File(buffer.toString());
        if (!storeFile.exists()) {
            flag = storeFile.mkdirs();
        }else{
            storeFile.delete();
        }
        if (!flag) {
            CreateFileException e = new CreateFileException(storeFile.getAbsolutePath() + "创建文件夹失败");
            log.warn(e.getMessage());
            throw e;
        }
        String imageId = LocalDateTime.now().format(fmt);
        buffer.append("/").append(imageId).append(file.getContentType());
        Files.createFile(Paths.get(buffer.toString()));
        file.transferTo(Paths.get(buffer.toString()));
        return code + "-" + timeId + "." + file.getContentType();
    }

    public void deleteImage(Integer id) throws IOException {
        String oldTitle = articleMapper.getOne(new QueryWrapper<Article>().select("title").eq("id", id.toString())).getTitle();
        File file = new File(imageDirName + "/");
        if (file.list() != null) {
            for (String name : file.list()) {
                if (name.substring(0, name.lastIndexOf("-")).equals(oldTitle)) {
                    Files.delete(Paths.get(imageDirName + name));
                }
            }
        }
    }


}