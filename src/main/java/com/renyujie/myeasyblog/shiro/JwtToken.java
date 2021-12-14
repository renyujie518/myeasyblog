package com.renyujie.myeasyblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName JwtToken.java
 * @Description 自定义Jwt->Token
 * @createTime 2021年12月03日 15:31:00
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    //秘钥也发返回token
    public Object getCredentials() {
        return token;
    }
}
