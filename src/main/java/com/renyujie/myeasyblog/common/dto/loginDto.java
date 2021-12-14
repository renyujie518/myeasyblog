package com.renyujie.myeasyblog.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName loginDto.java
 * @Description login实体类  前端传过来中所以要用到用户信息
 * @createTime 2021年12月07日 21:28:00
 */
@Data
public class loginDto  implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
