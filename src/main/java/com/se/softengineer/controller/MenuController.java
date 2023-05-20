package com.se.softengineer.controller;


import com.se.softengineer.entity.Menu;
import com.se.softengineer.service.MenuService;
import com.se.softengineer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lmy
 */

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/findByRoleId")
    public Result findById(@RequestParam Integer menuRight){
        List list = menuService.lambdaQuery().eq(Menu::getMenuRight,menuRight).list();
        return Result.success(list);
    }
}
