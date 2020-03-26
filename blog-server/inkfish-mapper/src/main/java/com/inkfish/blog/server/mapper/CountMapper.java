package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.model.pojo.Count;
import org.springframework.stereotype.Repository;

/**
 * @author HALOXIAO
 **/

@Repository
public class CountMapper extends ServiceImpl<CountDao, Count> {
}
