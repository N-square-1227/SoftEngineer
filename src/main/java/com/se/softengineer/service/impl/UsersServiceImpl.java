package com.se.softengineer.service.impl;

import com.se.softengineer.algorithm.EntropyWeight.Entropy;
import com.se.softengineer.algorithm.Kmeans.Cluster;
import com.se.softengineer.algorithm.Kmeans.Implement;
import com.se.softengineer.algorithm.Kmeans.Point;
import com.se.softengineer.algorithm.dataprocess.DataNumpy;
import com.se.softengineer.algorithm.indexsym.Data;
import com.se.softengineer.algorithm.indexsym.Node;
import com.se.softengineer.algorithm.trydatabase.TestMySQL;
import com.se.softengineer.dao.DataMapper;
import com.se.softengineer.dao.UsersMapper;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.NodeService;
import com.se.softengineer.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * @param users 用户实体
     * @return 插入成功与否
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
    public boolean Entropy() throws Exception {
        // 初始化算法类
        Entropy entropy = new Entropy();
        TestMySQL testMySQL = new TestMySQL();

        // todo: 数据表名
        // 获取 Node （indexsym）表
        List<List<Double>> data2D = testMySQL.queryData("data");
        // 转置数组
        Data data = new Data(data2D);
        DataNumpy.transposition(data);
        /* 处理数据并填充到 entropy 中 */
        // 把二维的数据表传进去
        entropy.setData2D(data.getData());
        entropy.setStdY2D(data.getData());
        // 指标的个数
        entropy.setIndexNumber(data.getData().size());
        // 计算每个指标子指标的个数并填充到 entropy 中
        List<Node> node = nodeService.queryNodeList();
        entropy.setNode(node);
        entropy.fillMap(node);

        // 调用算法类的熵权法
        entropy.algorithm();

        // 建表
        // todo:修改表名为前端传回来的数据！！！
        dataMapper.dropExistTable("nxntest");
        dataMapper.createTable("nxntest");
        // 将新的指标体系存到数据库的新表里
        return nodeService.insertIntoSheet("nxntest", entropy.getNode());
    }

    /**
     * kmeans算法调用
     * @author xly
     * @return
     * @throws Exception
     */
    public boolean kmeans() throws Exception {
        Implement implement = new Implement();
        Set<Cluster> clusterSet = implement.run();
        List<Node> nodeList = new ArrayList<>();
        int centerNum = clusterSet.size();
        for(int i=1;i<=centerNum;i++){
            Node node = new Node(i,"father"+i,1,1.0,0);
            nodeList.add(node);
        }
        int num = 1,i=1;
        for(Cluster cluster:clusterSet){
            List<Point> pointList = cluster.getMembers();
            for(Point point:pointList){
                Node node = new Node(centerNum+1,"X"+i,1,1.0,num);
                nodeList.add(node);
                centerNum += 1;
                i += 1;
            }
            num += 1;
        }
        dataMapper.dropExistTable("xlytest");
        dataMapper.createTable("xlytest");
        // 将新的指标体系存到数据库的新表里
        return nodeService.insertIntoSheet("xlytest", nodeList);
    }

//    /**
//     * @Author 南希诺
//     * @create 2023/5/10
//     * 从数据库里读出数据，按列读，全部存到一个数组里
//     * @return 数组
//     */
//    public List<List<Double>> getXColumns(Entropy entropy) {
//        List<List<Double>>  dataList = new ArrayList<>(500);
////        // todo:指标的个数，这个值在以后需要修改
////        int indexNumber = entropy.getIndexNumber();
////        entropy.setIndexNumber(indexNumber);
//        List<data> allData = dataMapper.selectList(null);
//
//        for (int i = 0; i < )
//        for (int i = 1; i <= indexNumber; ++i) {
//            // 创建新的 data 类型的查询语句
//            QueryWrapper<data> queryWrapper = new QueryWrapper<>();
//            List<Object> objList = dataMapper.selectObjs(queryWrapper);
//            List<Double> temp = new ArrayList<>();
//            // todo:给指标的个数赋值
//            // entropy.setIdxChild(objList.size());
//            if (objList.size() != 0) {
//                // 存储值
//                for (Object o : objList) {
//                    Double val = Double.valueOf(o.toString());
//                    temp.add(val);
//                }
//                dataList.add(temp);
//            }
//        }
//        return dataList;
//    }

    /**
     * 南希诺
     * 把我新的指标体系存到数据库里
     */
    public List<Node> saveEntropy() {
//        QueryWrapper<Node> queryWrapper = new QueryWrapper<>();
//        return nodeMapper.selectList(queryWrapper);
        //NodeServiceImpl nodeService = new NodeServiceImpl();
//        System.out.println(nodeService.queryNodeList());
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
