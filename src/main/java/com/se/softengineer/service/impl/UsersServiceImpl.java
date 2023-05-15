package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Users;
import com.se.softengineer.dao.UsersMapper;
import com.se.softengineer.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 **/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper,Users> implements UsersService {

    @Autowired
    private UsersMapper usersMapper;
    // 数据加密，在启动类中已经注入进IOC容器中
//    @Autowired
//    private BCryptPasswordEncoder encoder;
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
     * @author lmy
     */

    @Override
    public Users userLogin(String username, String password) {
        List list = lambdaQuery()
                .eq(Users::getUserName,username)
                .eq(Users::getUserPassword,password).list();
        Users user = null;
        if(list.size()>0)
            user = (Users)list.get(0);
        return user;
    }

    /**
     * @author lmy
     */

    private List<Users> getUserListByName(String name){
        return lambdaQuery().eq(Users::getUserName,name).list();
    }

    /**
     * @author lmy
     */

    @Override
    public Users userRegister(String username, String password, String email) {
        List<Users> list = getUserListByName(username);
        if(list.size()>0)
            return null;
        Users user = new Users();
        user.setUserName(username);
        user.setUserPassword(password);
        user.setUserEmail(email);
        user.setRole("2");
        int i = usersMapper.insert(user);
        return i==1?user:null;
    }

    /**
     * @author lmy
     */

    public Users updateUser(Users user) {
        List<Users> list = getUserListByName(user.getUserName());
        //用户名存在(即id不一致)
        if(list.size()>0 && list.get(0).getUserID()!=user.getUserID())
            return null;
        int i = usersMapper.updateById(user);
        if(i==1)
            return user;
        return null;
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
