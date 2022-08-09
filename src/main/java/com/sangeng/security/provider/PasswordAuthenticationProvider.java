package com.sangeng.security.provider;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangeng.security.domain.LoginUser;
import com.sangeng.security.domain.User;
import com.sangeng.security.handler.SmsAuthenticationToken;
import com.sangeng.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * @Auther:yukemeng Date:2022/4/14-04-14-16:51
 * Description:
 * version:1.0
 */
@Component
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public PasswordEncoder passwordEncoder;


    //
    public PasswordAuthenticationProvider(@Qualifier("passwordUserDetailService") UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 认证的过程
        Object principal = authentication.getPrincipal();
        String username = "";
        if (principal instanceof String) {
            username = (String) principal;
        }
        String password = (String) authentication.getCredentials();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        User user = userMapper.selectOne(wrapper);

        if(Objects.isNull(user)){
            throw  new UsernameNotFoundException("用户不存在");
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if(!matches){
            throw new UsernameNotFoundException("密码错误，请重新填写密码");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 认证通过之后
        // 3. 重新创建已认证对象
        UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(principal,password, userDetails.getAuthorities());
        authenticationResult.setDetails(userDetails);
        return authenticationResult;
    }

    // 定义哪个类可以使用这个Provider
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
