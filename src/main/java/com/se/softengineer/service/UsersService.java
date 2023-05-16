package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.mapper.UsersMapper;
import com.se.softengineer.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 **/
public interface UsersService extends IService<Users> {

    /**
     * 登录
     * @author lmy
     */
    public Users userLogin(String username,String password);

    /**
     * 注册
     * @author lmy
     */
    public Users userRegister(String username, String password, String email);

    /**
     * 修改
     * @author lmy
     */
    public Users updateUser(Users user);

}
