package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.se.softengineer.algorithm.EntropyWeight.Entropy;
import com.se.softengineer.algorithm.indexsym.Node;
import com.se.softengineer.dao.DataMapper;
import com.se.softengineer.dao.NodeMapper;
import com.se.softengineer.dao.UsersMapper;
import com.se.softengineer.entity.Users;
import com.se.softengineer.entity.data;
import com.se.softengineer.service.NodeService;
import com.se.softengineer.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 **/
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private NodeService nodeService;

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
     * @Author 南希诺
     * @create 2023.5.10
     * 熵权法
     * @return 成功处理 true，else false
     */
    @Override
    public boolean Entropy() {
        // 初始化算法类
        Entropy entropy = new Entropy();
        // 获取数据库数据的数组并填充算法中数组的值
        List<Double> objList = getXColumns(entropy);

        entropy.setDataList(objList);
        entropy.setEntropyList(saveEntropy());

        // 调用算法类的熵权法
        entropy.algorithm();

        return true;
    }

    /**
     * @Author 南希诺
     * @create 2023/5/10
     * 从数据库里读出数据，按列读，全部存到一个数组里
     * @return 数组
     */
    public List<Double> getXColumns(Entropy entropy) {
        List<Double> dataList = new ArrayList<>(500);
        // todo:指标的个数，这个值在以后需要修改
        int indexNumber = entropy.getIndexNumber();
        entropy.setIndexNumber(indexNumber);
        for (int i = 1; i <= indexNumber; ++i) {
            // 创建新的 data 类型的查询语句
            QueryWrapper<data> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(String.format("X%d", i));
            List<Object> objList = dataMapper.selectObjs(queryWrapper);
            // 给指标的个数赋值
            entropy.setIdxChild(objList.size());
            if (objList.size() != 0) {
                // 存储值
                for (Object o : objList) {
                    Double val = Double.valueOf(o.toString());
                    dataList.add(val);
                }
            }
        }
        return dataList;
    }

    /**
     * 把我新的指标体系存到数据库里
     */
    public List<Node> saveEntropy() {
//        QueryWrapper<Node> queryWrapper = new QueryWrapper<>();
//        return nodeMapper.selectList(queryWrapper);
        //NodeServiceImpl nodeService = new NodeServiceImpl();
        return nodeService.queryNodeList();
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
