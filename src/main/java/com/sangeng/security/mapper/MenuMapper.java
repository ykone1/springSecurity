package com.sangeng.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.security.domain.Menu;

import java.util.List;

/**
 * @Auther:yukemeng Date:2022/4/13-04-13-21:21
 * Description:
 * version:1.0
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);
}
