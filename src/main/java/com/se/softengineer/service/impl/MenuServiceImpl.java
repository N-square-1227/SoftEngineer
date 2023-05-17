package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.mapper.MenuMapper;
import com.se.softengineer.entity.Menu;
import com.se.softengineer.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 */

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
