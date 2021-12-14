package com.renyujie.myeasyblog.shiro;

import cn.hutool.json.JSONUtil;
import com.renyujie.myeasyblog.common.lang.Result;
import com.renyujie.myeasyblog.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName JwtFilter.java
 * @Description 自定义的JwtFilter  第一道关卡
 * @createTime 2021年12月03日 15:24:00
 */
@Component
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtils jwtUtils;



    //通过前端传来的JWt(header里的Authorization字段) 生成shiro可识别的AuthenticationToken（JWt->token） 这里采用自定义的token
    //点进去可以发现AuthenticationToken是个接口  需要自定义token重写接口内的两个方法：JwtToken
    //在AuthenticatingFilter方法里会有login(token)的操作
    //接着会进入AccountRealm（继承了AuthorizingRealm）的doGetAuthenticationInfo方法里对这个封装好的AuthenticationToken处理，再交给shiro处理
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest httpServerRequest = (HttpServletRequest) servletRequest;
        String jwt = httpServerRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)) {
            return null;
        }
        return new JwtToken(jwt);

    }


    //拦截判断 jwt凭证是否过期， 正确等
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpServerRequest = (HttpServletRequest) servletRequest;
        String jwt = httpServerRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)) {
            return true;  //如果是没传来jwt，直接进controller
        } else {
            //校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {  //判断有没有过期
                throw new ExpiredCredentialsException("token失效，请重新登录");
            }
            //正常的jwt，执行登录(进入上面的那个createToken方法，生成AuthenticationToken)
            return executeLogin(servletRequest, servletResponse);
        }
    }


    //要是上一个方法中的executeLogin执行失败，用该要做统一异常处理，所以重写此方法
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //获取异常原因
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result failResult = Result.fail(throwable.getMessage());
        //要以json形式返回  用hutool工具
        String jsonStr = JSONUtil.toJsonStr(failResult);
        try {
            httpServletResponse.getWriter().print(jsonStr);
        } catch (IOException ioException) {
           //没有成功写到给前端的response里不用处理
        }
        //做完统一异常处理，返回onLoginFailure = false的信号
        return false;
    }


    //解决跨域问题
    //客户端请求经过的先后顺序问题，当服务端接收到一个请求时
    //该请求会先经过过滤器，然后进入拦截器中，然后再进入Mapping映射中的路径所指向的资源
    //所以在过滤器中要重写preHandle  这一段基本是标准写法
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
