package com.renyujie.myeasyblog.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName Result.java
 * @Description 统一封装类  用于返给前端正确/错误信息
 * @createTime 2021年12月03日 12:58:00
 */
@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result resultMsg(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result succ(Object data) {
        return resultMsg(200, "操作成功", data);
    }

    public static Result fail(String msg) {
        return resultMsg(400, msg, null);
    }
    public static Result fail(int code, String msg, Object data) {
        return resultMsg(code, msg, data);
    }
}

