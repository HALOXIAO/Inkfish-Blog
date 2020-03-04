package com.inkfish.blog.server.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.server.common.exception.CreateFileException;
import com.inkfish.blog.server.mapper.ArticleMapper;
import com.inkfish.blog.server.model.pojo.Article;
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

    private static final String imageDirName = "../../Image";

    public String addImage(MultipartFile file, String title, Integer id) throws IOException {
        if (id != null) {
            deleteImage(id);
        }
        boolean flag = true;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");//设置日期格式
        String timeId = LocalDateTime.now().format(fmt);
        String code = Integer.toString(title.hashCode());
        StringBuilder Stringbuilder = new StringBuilder();
        Stringbuilder.append(imageDirName).append("/").append(code).append("-")
                .append(timeId);
        File storeFile = new File(Stringbuilder.toString());
        if (!storeFile.exists()) {
            flag = storeFile.mkdirs();
        } else {
            storeFile.delete();
        }
        if (!flag) {
            CreateFileException e = new CreateFileException(storeFile.getAbsolutePath() + "创建文件夹失败");
            log.warn(e.getMessage());
            throw e;
        }
        String imageId = LocalDateTime.now().format(fmt);
        Stringbuilder.append("/").append(imageId).append("-").append(file.getOriginalFilename());
        Files.createFile(Paths.get(Stringbuilder.toString()));
        file.transferTo(Paths.get(Stringbuilder.toString()));
        return code + "-" + timeId + "/" + imageId + "-" + (file.getOriginalFilename());
    }




    // Need To Change ...... Maybe?
    public void deleteImage(Integer id) throws IOException {
        Article article = articleMapper.getOne(new QueryWrapper<Article>().select("title").eq("id", id.toString()));
        if (article == null) {
            return;
        }
        String oldTitle = article.getTitle();
        File file = new File(imageDirName + "/");
        if (file.list() != null || file.list().length > 0) {
            for (int i = file.list().length - 1; i >= 0; i--) {
                String name = file.list()[i];
                if (name.substring(0, name.lastIndexOf("-")).equals(oldTitle)) {
                    Files.delete(Paths.get(imageDirName + "/" + name));
                    break;
                }
            }

        }
    }


}