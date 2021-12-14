package com.renyujie.myeasyblog.service.impl;

import com.renyujie.myeasyblog.entity.User;
import com.renyujie.myeasyblog.mapper.UserMapper;
import com.renyujie.myeasyblog.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
