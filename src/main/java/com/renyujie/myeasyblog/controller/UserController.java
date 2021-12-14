package com.renyujie.myeasyblog.controller;


import com.renyujie.myeasyblog.common.lang.Result;
import com.renyujie.myeasyblog.entity.User;
import com.renyujie.myeasyblog.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-02
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;


    //测试  用于检验自动代码生成的pojo是否有效
    //@RequiresAuthentication
    @GetMapping("/test1")
    public Object text1() {
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    //测试  用于测试@valided注解的实体校验
    @PostMapping("/test2")
    Result text2(@Validated @RequestBody User user) {
        return Result.succ(user);
    }

}
