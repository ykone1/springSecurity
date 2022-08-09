package com.sangeng.security.config;

import com.sangeng.security.domain.LoginUser;
import com.sangeng.security.filter.JwtAuthenticationTokenFilter;
import com.sangeng.security.provider.PasswordAuthenticationProvider;
import com.sangeng.security.provider.SmsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-22:04
 * Description:
 * version:1.0
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 使用访问前判断是否需要权限
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean  // passwordEncoder 用来密码匹配的。
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean  //  将 AuthenticationManager 暴露出来，这个类可以调用 认证链
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;



    @Autowired
   // private SmsAuthenticationProvider smsAuthenticationProvider;


    @Override // 配置认证相关的内容
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login","/user/login/sms","/user/login/check").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 配置过滤器链
        http
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 配置认证失败，认证成功的处理器
        http.exceptionHandling()

                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        // 允许跨域请求
        http.cors();

    }

    @Autowired
    private SmsAuthenticationProvider smsAuthenticationProvider;

    @Autowired
    private PasswordAuthenticationProvider passwordAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .authenticationProvider(smsAuthenticationProvider)
                .authenticationProvider(passwordAuthenticationProvider);

    }


}
