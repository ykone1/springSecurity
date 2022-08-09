package com.sangeng.security.provider;

import com.sangeng.security.handler.SmsAuthenticationToken;
import com.sangeng.security.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private static final String prefix = "CHECE_CODE";

    // 定义了调用哪一个UserDeatailService
    private UserDetailsService userDetailsServiceImpl;

    @Autowired
    private RedisCache redisCache;

    public SmsAuthenticationProvider(@Qualifier("smsUserDetailsService") UserDetailsService userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        Object principal = authentication.getPrincipal();// 获取凭证也就是用户的手机号
        String phone = "";
        if (principal instanceof String) {
            phone = (String) principal;
        }
        String inputCode = (String) authentication.getCredentials(); // 获取输入的验证码
        // 1. 检验Redis手机号的验证码
        String redisCode = redisCache.getCacheObject(prefix + phone);
        if (Objects.isNull(redisCode)) {
            throw new BadCredentialsException("验证码已经过期或尚未发送，请重新发送验证码");
        }
        if (!inputCode.equals(redisCode)) {
            throw new BadCredentialsException("输入的验证码不正确，请重新输入");
        }
        // 2. 根据手机号查询用户信息
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(phone);
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("phone用户不存在，请注册");
        }
         // 3. 重新创建已认证对象,
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(principal,inputCode, userDetails.getAuthorities());
        authenticationResult.setDetails(userDetails);
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(SmsAuthenticationToken.class);
    }
}
