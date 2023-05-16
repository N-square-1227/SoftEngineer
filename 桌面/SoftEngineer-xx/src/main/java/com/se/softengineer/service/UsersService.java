package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.dao.UsersMapper;
import com.se.softengineer.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 **/
public interface UsersService extends IService<Users> {
//    /**
//     * 书城条件分页查询
//     * @param page
//     * @param wrapper
//     * @return
//     */
//    Page<Book> selectPage(Page<Book> page, QueryWrapper<Book> wrapper);

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
