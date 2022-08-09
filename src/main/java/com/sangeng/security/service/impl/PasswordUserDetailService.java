package com.sangeng.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangeng.security.domain.LoginUser;
import com.sangeng.security.domain.User;
import com.sangeng.security.mapper.MenuMapper;
import com.sangeng.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther:yukemeng Date:2022/4/14-04-14-17:37
 * Description:
 * version:1.0
 */
@Service("passwordUserDetailService")
public class PasswordUserDetailService implements UserDetailsService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 数据库查询手机用户,这里需要写根据手机号查询用户信息，这里写死吧。。。

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }

        // 2. 角色权限集合转为  List<GrantedAuthority>
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        // 3. 返回自定义的用户信息
        return new LoginUser(user,list);
    }
}
