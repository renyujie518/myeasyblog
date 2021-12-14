package com.renyujie.myeasyblog.common.exception;

import com.renyujie.myeasyblog.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName globalExceptionHandle.java
 * @Description 全局异常处理
 * @createTime 2021年12月05日 20:31:00
 */
@Slf4j
@RestControllerAdvice
public class globalExceptionHandle {
    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常---{}", e);
        return Result.fail(400,e.getMessage(),null);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)  //401  无权限
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e) {
        log.error("shiro异常---{}", e);
        return Result.fail(401, e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            if (errors != null) {
                errors.forEach(
                        p -> {
                            String defaultMessage = p.getDefaultMessage();
                            log.error("实体校验异常---{}", defaultMessage);
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append(",");
                            }
                            stringBuilder.append(defaultMessage);
                        });
            }
        }
        return Result.fail(stringBuilder.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常---{}", e);
        return Result.fail(400, e.getMessage(), null);
    }

}
