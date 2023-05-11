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

//    /**
//     * 根据 id 获取书本信息
//     * @param id
//     * @return
//     */
//    Book getOneBook(Integer id);

//    /**
//     * 删除一个用户
//     * @param book
//     * @return
//     */
//    int deleteOneBook(Book book);
//
//    /**
//     * 修改一本书的信息
//     * @param book
//     * @return
//     */
//    int updOneBook(Book book);
}
