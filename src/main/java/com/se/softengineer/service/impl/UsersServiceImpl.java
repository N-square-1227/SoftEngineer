package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.se.softengineer.algorithm.EntropyWeight.Entropy;
import com.se.softengineer.dao.DataMapper;
import com.se.softengineer.entity.Users;
import com.se.softengineer.dao.UsersMapper;
import com.se.softengineer.entity.data;
import com.se.softengineer.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 **/
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private DataMapper dataMapper;

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
     * 增加一个用户的信息
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

    /**
     * 熵权法
     * @return
     */
    @Override
    public boolean Entropy() {
        // 初始化算法类
        Entropy entropy = new Entropy();

        List<Double> objList = getXColumns();
        // 填充数组的值
        entropy.setDataList(objList);
        // 调用算法类的熵权法
        entropy.algorithm();
        return true;
    }

    public List<Double> getXColumns() {
        List<Double> dataList = new ArrayList<>();
        for (int i = 1; i <= 23; ++i) {
            QueryWrapper<data> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(String.format("X%d", i));
            List<Object> objList = dataMapper.selectObjs(queryWrapper);
            if (objList.size() != 0) {
                for (Object o : objList) {
                    Double val = Double.valueOf(o.toString());
                    dataList.add(val);
                }
            }
        }
        return dataList;
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
