package com.renyujie.myeasyblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.renyujie.myeasyblog.common.dto.loginDto;
import com.renyujie.myeasyblog.common.lang.Result;
import com.renyujie.myeasyblog.entity.User;
import com.renyujie.myeasyblog.service.UserService;
import com.renyujie.myeasyblog.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName AccountController.java
 * @Description 账户密码的Controller
 * @createTime 2021年12月07日 21:22:00
 */
@RestController
public class AccountController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/login")
    public Result logiin(@Validated @RequestBody loginDto loginDto, HttpServletResponse response) {

        //利用RequestBody的username从userService的查找对应列的信息  注意，这个代表数据库中真实的user信息
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        //断言检查user,如果为null就返回IllegalArgumentException异常信息
//        Assert.notNull(user, "用户不存在");
        if (user == null) {
            return Result.fail("用户不存在");
        }
//        //判断用户
//        if (!user.getUsername().equals(loginDto.getUsername())) {
//            return Result.fail("用户名不正确");
//        }
        //判断密码
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        //密码正确就生成JWT返给前端，利用用户ID
        String jwt = jwtUtils.generateToken(user.getId());
        //本项目把JWY放在header中  考虑如果JWT如果要延期  直接从header操作即可，否则要再写一个延期接口
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");


        //以上都没问问题，返回用户信息
        return Result.succ(MapUtil.builder()
                .put("id",user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }

    //退出
    //logout()需要验证权限
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}
