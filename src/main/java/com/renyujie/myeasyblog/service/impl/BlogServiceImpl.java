package com.renyujie.myeasyblog.service.impl;

import com.renyujie.myeasyblog.entity.Blog;
import com.renyujie.myeasyblog.mapper.BlogMapper;
import com.renyujie.myeasyblog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-02
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
