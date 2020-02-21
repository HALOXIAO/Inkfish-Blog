package com.inkfish.blog.web;

import com.inkfish.blog.mapper.ArticleTagMapper;
import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.model.pojo.Article;
import com.inkfish.blog.model.pojo.ArticleTag;
import com.inkfish.blog.service.ArticleService;
import org.apache.ibatis.jdbc.SQL;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Component
class InkfishWebApplicationTests {


    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleTagMapper tagMapper;

    @Autowired
    ArticleTagRelationMapper mapper;

    @Test
    void contextLoads() throws IOException {
        ArticleTag tag = new ArticleTag();
        tag.setName("tag1");
        List<ArticleTag>tags = new ArrayList<>();
        tags.add(tag);

        //INSERT INTO article_and_tag_relation (article_id,tag_id) SELECT 15,id FROM article_tag WHERE id=4
        System.out.println( new SQL() {
            {
                INSERT_INTO("article_and_tag_relation (article_id,tag_id)");
                StringBuffer buffer = new StringBuffer("SELECT ");
                buffer.append("15").append(",id FROM article_tag WHERE name IN (");
                tags.parallelStream().forEach(
                        p -> {
                            buffer.append(p.getName()).append(",");
                        }
                );
                buffer.deleteCharAt(buffer.lastIndexOf(","));
                buffer.append(")");
                INTO_COLUMNS(buffer.toString());
            }

        }.toString());
    }
}
