package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.server.model.pojo.Count;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface CountDao extends BaseMapper<Count> {

    @Update("UPDATE count SET article_total = article_total+1 where id=0")
    boolean articleIncre();

    @Update("UPDATE count SET tag_total = tag_total+1 where id = 0")
    boolean tagIncre();

    @Update("UPDATE count SET article_total = article_total-1  where id=0 AND article_total>0")
    boolean articleDece();

    @Update("UPDATE count SET tag_total = tag_total-1  where id=0 AND tag_total > 0")
    boolean tagDece();


}
