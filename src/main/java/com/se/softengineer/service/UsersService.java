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
    Users userLogin(String username,String password) throws Exception;

    /**
     * 注册
     * @author lmy
     */
    Users userRegister(String username, String password, String email) throws Exception;

    /**
     * 修改
     * @author lmy
     */
    Users updateUser(Users user) throws Exception;

    Boolean updateUserLoginTime(Users user);
}
