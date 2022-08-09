package com.sangeng.security.filter;

import com.sangeng.security.domain.LoginUser;
import com.sangeng.security.utils.JwtUtil;
import com.sangeng.security.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Auther:yukemeng Date:2022/4/13-04-13-10:49
 * Description:
 * version:1.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    // java web 中 实现 filter 接口
    // 这里集成 OncePerRequestFilter 保证过滤器只通过一次

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 获取token
        String token = httpServletRequest.getHeader("token");

        if (!StringUtils.hasText(token)) {
            // 必须要放行，因为有登录页面没有没有token
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            // 放行结束后，出过滤器时，还会继续执行下面的内容。
            return;
        }
        // 解析token
        String userId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception exception) {
            exception.printStackTrace();
            // 实际环境应该封装类，返回前端。
            throw new RuntimeException("token非法");
        }
        // 从redis中获取信息
        String stringKey = "login" + userId;
        LoginUser loginUser = redisCache.getCacheObject(stringKey);
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        // 存入到 SecurityContextHolder 中 ，授权时可以使用


        // 色织
        // 三参数是已经认证了，就不要ProviderManager去认证了，两个参数需要ProviderMangeger认证。
        // 双参是未认证的，会去认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken( loginUser,null,loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(httpServletRequest,httpServletResponse); // 放行
    }
}
