package com.se.softengineer.service.impl;

import com.se.softengineer.entity.Users;
import com.se.softengineer.dao.UsersMapper;
import com.se.softengineer.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 **/
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

//    /**
//     * 书城条件分页查询
//     *
//     * @param page
//     * @param wrapper
//     * @return
//     */
//    @Override
//    public Page<Book> selectPage(Page<Book> page, QueryWrapper<Book> wrapper) {
//        return bookMapper.selectPage(page,wrapper);
//    }

    /**
     * 增加一本书的信息
     *
     * @param users
     * @return
     */
    @Override
    public int addUsersInfo(Users users) {
        Users entity = new Users();
        entity.setUserEmail(users.getUserEmail());
        entity.setUserName(users.getUserName());
        entity.setUserPassword(users.getUserPassword());
        return usersMapper.insert(entity);
    }

//    /**
//     * 根据id获取书本信息
//     *
//     * @param id
//     * @return
//     */
//    @Override
//    public Book getOneBook(Integer id) {
//        Book book = bookMapper.selectById(id);
//        return book;
//    }

//    /**
//     * 删除一本书
//     *
//     * @param book
//     * @return
//     */
//    @Override
//    public int deleteOneBook(Book book) {
//        Book entity = new Book();
//        entity.setId(book.getId());
//        entity.setIsDeleted(1);
//        entity.setGmtModified(TimeUtil.getTime());
//        return bookMapper.updateById(entity);
//    }
//
//    /**
//     * 修改一本书的信息
//     *
//     * @param book
//     * @return
//     */
//    @Override
//    public int updOneBook(Book book) {
//        Book entity = new Book();
//        entity.setId(book.getId());
//        entity.setPicture(book.getPicture());
//        entity.setName(book.getName());
//        entity.setIntroduce(book.getIntroduce());
//        entity.setPublish(book.getPublish());
//        entity.setAuth(book.getAuth());
//        entity.setPrice(book.getPrice());
//        entity.setGmtModified(TimeUtil.getTime());
//        return bookMapper.updateById(entity);
//    }
}
