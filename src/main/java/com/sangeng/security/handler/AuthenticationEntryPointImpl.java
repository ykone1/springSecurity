package com.sangeng.security.handler;

import com.alibaba.fastjson.JSON;
import com.sangeng.security.domain.ResponseResult;
import com.sangeng.security.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther:yukemeng Date:2022/4/13-04-13-21:48
 * Description:
 * version:1.0
 */
@Component  // 认证失败  当认证失败时，会调用此方法
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 处理异常
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(httpServletResponse,json);
    }
}
