package com.renyujie.myeasyblog.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName AccountProfile.java
 * @Description 账户信息  通常是除password外的一些基本信息
 * @createTime 2021年12月03日 22:02:00
 */

@Data
public class AccountProfile implements Serializable {
    private Long id;

    private String username;

    private String avatar;

    private String email;
}
