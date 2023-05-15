package com.se.softengineer.service;

import com.se.softengineer.entity.Users;

/**
 *
 **/
public interface UsersService {

//    /**
//     * 书城条件分页查询
//     * @param page
//     * @param wrapper
//     * @return
//     */
//    Page<Book> selectPage(Page<Book> page, QueryWrapper<Book> wrapper);

    /**
     * 增加一个用户的信息
     * @param users 用户实体
     * @return 成功 / 失败
     */
    int addUsersInfo(Users users);

    /**
     * @Author 南希诺
     * @create 2023.5.10
     * 熵权法
     * @return 成功处理 true，else false
     */
    boolean Entropy() throws Exception;
    boolean runkmeans() throws Exception;

}
