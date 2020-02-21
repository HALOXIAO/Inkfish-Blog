package com.inkfish.blog.mapper;

        import com.baomidou.mybatisplus.core.mapper.BaseMapper;
        import com.inkfish.blog.model.pojo.Article;
        import org.apache.ibatis.annotations.Mapper;
        import org.apache.ibatis.annotations.Select;

        import java.util.List;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface ArticleDao extends BaseMapper<Article> {

    @Select("SELECT id,title,overview,status FROM  article LIMIT #{page},#{size}")
    List<Article> getArticleOverview(Integer page, Integer size);

}
