package com.renyujie.myeasyblog.shiro;

import com.renyujie.myeasyblog.entity.User;
import com.renyujie.myeasyblog.service.UserService;
import com.renyujie.myeasyblog.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName AccountRealm.java
 * @Description Shiro将数据库中的数据，存放到Realm这种对象中,判定token是否和数据库中的一致
 * @createTime 2021年12月03日 14:36:00
 */
@Component
public class AccountRealm  extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;


    //doGetAuthenticationInfo强转为自定的jwtToken的时候要告诉Realm支持自定的jwtToken
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    //获取用户的权限信息并封装为AuthorizationInfo返给shiro（授权方法）
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    //依据获取的token信息组装为 AuthenticationInfo （认证方法）接下来shiro拿输入的token与当前返回的这个数据库凭证SimpleAuthenticationInfo对比一下
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //先把获取（前端传来的token）转化为自定义的JwtToken，注意，这个token包含着jwt信息
        JwtToken jwtToken = (JwtToken) token;
        //一般前端传来的userid是放在Claim的Subject字段里
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getCredentials()).getSubject();

        //这是从数据库中通过userid(代表账户)获得用户信息
        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }

        AccountProfile profile = new AccountProfile();
        //对应转移相同名字属性  BeanUtils.copyProperties("被赋值的对象", "被复制的对象");  这里相当于更新用户
        BeanUtils.copyProperties(user,profile);
        //把用户的基本信息封装返回给shiro
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());

    }
}
