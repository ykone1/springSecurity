package com.sangeng.security.service;

import com.sangeng.security.domain.ResponseResult;
import com.sangeng.security.domain.User;

import java.util.Map;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-23:10
 * Description:
 * version:1.0
 */
public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult getSMS(Map<String, Object> param);

    ResponseResult check(Map<String, Object> param);
}

