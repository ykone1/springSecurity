package com.sangeng.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangeng.security.domain.LoginUser;
import com.sangeng.security.domain.User;
import com.sangeng.security.mapper.MenuMapper;
import com.sangeng.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 短信登录 的 UserDetail类，
@Service("smsUserDetailsService")
public class SmsUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * @param phone 手机号
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        // 1. 数据库查询手机用户,这里需要写根据手机号查询用户信息，这里写死吧。。。

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phonenumber", phone);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new UsernameNotFoundException("手机号不存在！");
        }

        // 2. 角色权限集合转为  List<GrantedAuthority>
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        // 3. 返回自定义的用户信息
        return new LoginUser(user,list);
    }
}
