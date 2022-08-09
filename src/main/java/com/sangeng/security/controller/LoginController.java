package com.sangeng.security.controller;

import com.sangeng.security.domain.ResponseResult;
import com.sangeng.security.domain.User;
import com.sangeng.security.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-23:05
 * Description:
 * version:1.0
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        // 登录
        ResponseResult result =  loginService.login(user);

        return result;
    }

    /**
     * 退出登录接口
     * @return
     */
    @RequestMapping("/user/logout")
    public ResponseResult logout(){
        ResponseResult result =  loginService.logout();
        return result;
    }

    /**
     * 获取短信验证码
     * @return
     */
    @RequestMapping("/user/login/sms")
    public ResponseResult getSMS(@RequestBody Map<String,Object> param){
        ResponseResult result =  loginService.getSMS(param);
        return result;
    }

    /**
     * 短信验证登录
     * @param param
     * @return
     */
    @PostMapping("/user/login/check")
    public ResponseResult checkSMS(@RequestBody Map<String,Object> param){
        ResponseResult result =  loginService.check(param);
        return result;
    }
}
