package com.sangeng.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangeng.security.domain.LoginUser;
import com.sangeng.security.domain.User;
import com.sangeng.security.mapper.MenuMapper;
import com.sangeng.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-21:32
 * Description:
 * version:1.0
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private MenuMapper menuMapper;

    // 上层都会调用这个方法。查询的结果会封装到UserDetail中
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return loadByUsernamePassword(s);
    }

    private UserDetails loadByUsernamePassword(String username){
        // s 为前端传的用户名
        // 查询用户和密码
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        User user = userMapper.selectOne(queryWrapper);

        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 查询权限信息
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        // 把数据封装成UserDetails

        return new LoginUser(user,list);
    }
}
