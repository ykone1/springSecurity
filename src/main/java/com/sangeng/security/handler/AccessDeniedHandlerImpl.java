package com.sangeng.security.handler;

import com.alibaba.fastjson.JSON;
import com.sangeng.security.domain.ResponseResult;
import com.sangeng.security.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther:yukemeng Date:2022/4/13-04-13-22:02
 * Description:
 * version:1.0
 */
@Component  // 授权失败 抛出异常，会调用此方法
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(),"权限不足");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(httpServletResponse,json);
    }
}
