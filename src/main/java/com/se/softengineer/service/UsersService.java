package com.se.softengineer.service;

import com.se.softengineer.entity.Users;

/**
 *
 **/
public interface UsersService {

    /**
     * 增加一个用户的信息
     * @param users 用户实体
     * @return 成功 / 失败
     */
    int addUsersInfo(Users users);



}
