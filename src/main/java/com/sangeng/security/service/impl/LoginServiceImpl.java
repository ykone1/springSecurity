package com.sangeng.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sangeng.security.domain.LoginUser;
import com.sangeng.security.domain.ResponseResult;
import com.sangeng.security.domain.User;
import com.sangeng.security.handler.SmsAuthenticationToken;
import com.sangeng.security.service.LoginService;
import com.sangeng.security.utils.JwtUtil;
import com.sangeng.security.utils.RedisCache;
import com.sangeng.security.utils.SMSUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-23:11
 * Description:
 * version:1.0
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private static final String prefix = "CHECE_CODE";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SMSUtils smsUtils;


    /**
     *  验证登录，登录成功后，生成token并放入Redis缓存中
     * @param user 用户
     * @return
     */
    @Override
    public ResponseResult login(User user) {

        // 这一步调用完成后，已经获得了UserDetail类型的数，具体类型为LoginUserDetail
        // UserDetailServiceImpl 返回的 UserDetail 会与 传入的用户名和密码进行比对。比对时使用BCry 带加密比较。
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 登录失败
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名" + user.getUserName() + "或密码错误，登录失败");
        }

        LoginUser loginUser = (LoginUser) authenticate.getDetails();
        // Object details = authenticate.getDetails();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        // 放入到Redis缓存中, 这里没有存放token，存储的是 detail对象
        redisCache.setCacheObject("login" + userId,loginUser);

        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        return new ResponseResult(200, "登录成功", map);
    }

    @Override
    public ResponseResult logout() {
        // 从SecurityContextHolder 中获取 认证的用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(Objects.isNull(loginUser)){
            return new ResponseResult(200,"用户没登录");
        }
        // 从redis中删除
        String stringKey = "login" + loginUser.getUser().getId().toString();
        redisCache.deleteObject(stringKey);

        return new ResponseResult(200,"注销成功");
    }

    /**
     * 生成验证码
     * @param param 包含手机号
     * @return
     */
    @Override
    public ResponseResult getSMS(Map<String, Object> param) {

        String phone = param.get("phone").toString();

        String code = smsUtils.sendSmsAliyun(phone);
        if(Objects.isNull(code)){
            return new ResponseResult(500,"验证码获取失败");
        }

        try {
            redisCache.setCacheObject(prefix + phone , code);
        } catch (Exception exception) {
            log.error("验证码缓存进Redis失败：" + phone);
            exception.printStackTrace();
        }

        return new ResponseResult(200,"获取验证码成功",code);
    }

    @Override
    public ResponseResult check(Map<String, Object> param) {
        // 这一步调用完成后，已经获得了UserDetail类型的数，具体类型为LoginUserDetail
        // UserDetailServiceImpl 返回的 UserDetail 会与 传入的用户名和密码进行比对。比对时使用BCry 带加密比较。
       SmsAuthenticationToken authenticationToken
                = new SmsAuthenticationToken(param.get("phone"),param.get("code"));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 登录失败
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("手机号" + param.get("phone") + "或密码错误，登录失败");
        }

        LoginUser loginUser = (LoginUser) authenticate.getDetails();
        // Object details = authenticate.getDetails();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        // 放入到Redis缓存中, 这里没有存放token，存储的是 detail对象
        redisCache.setCacheObject("login" + userId,loginUser);

        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        return new ResponseResult(200, "登录成功", map);
    }
}
