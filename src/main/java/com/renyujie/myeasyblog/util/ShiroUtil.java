package com.renyujie.myeasyblog.util;

import com.renyujie.myeasyblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName ShiroUtil.java
 * @Description Shiro工具类  获取Subject（userid）的Principal
 * @createTime 2021年12月07日 22:23:00
 */
public class ShiroUtil {
    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
