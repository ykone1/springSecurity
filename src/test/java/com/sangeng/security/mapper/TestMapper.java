package com.sangeng.security.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangeng.security.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-21:26
 * Description:
 * version:1.0
 */
@SpringBootTest
public class TestMapper {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMapper(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", "sg");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    public void testBCryPasswordEncode(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String encode = bCryptPasswordEncoder.encode("1234");
        String encode2 = bCryptPasswordEncoder.encode("1234");
        // $2a$10$MY.gUm1SFrTBSD6pB5/BUOSWkm.28SrNWuCoNycy/lnMPFpMCFoKG
        System.out.println(encode);
    }

    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void testSelectPermsByUserId(){
        List<String> list = menuMapper.selectPermsByUserId(2L);
        System.out.println(list);
    }
}
