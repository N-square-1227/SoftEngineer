package com.se.softengineer.service.impl;

import com.se.softengineer.algorithm.EntropyWeight.Entropy;
import com.se.softengineer.algorithm.Kmeans.*;
import com.se.softengineer.algorithm.dataprocess.DataNumpy;
import com.se.softengineer.algorithm.indexsym.Data;
import com.se.softengineer.entity.Node;
import com.se.softengineer.algorithm.trydatabase.TestMySQL;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.mapper.DataMapper;
import com.se.softengineer.mapper.UsersMapper;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.NodeService;
import com.se.softengineer.service.SampleService;
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

    @Autowired
    private SampleService sampleService;

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

        List<Sample> sampleList = new ArrayList<>();
        List<List<Double>> data = new ArrayList<>();
        //这里的data需要从前端传回来
        sampleList = sampleService.getData("data");
        List<String> columnList = new ArrayList<>();//指标个数
        columnList = sampleService.getColName("data");
        for(Sample sample : sampleList) {
            data.add(sample.getData());
        }

        DataNumpy.transposition(data);
        /* 处理数据并填充到 entropy 中 */
        // 把二维的数据表传进去
        entropy.setData2D(data);
        entropy.setStdY2D(data);
        // 指标的个数
        entropy.setIndexNumber(columnList.size());
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

    /**
     * kmeans算法调用
     * @author xly
     * @return
     * @throws Exception
     */
    @Override
    public boolean runkmeans() throws Exception {
        List<Sample> sampleList,testList = new ArrayList<>();
        //这里的data需要从前端传回来
        sampleList = sampleService.getData("data");
        testList = sampleService.getData("data");
        List<String> columnList = new ArrayList<>();
        columnList = sampleService.getColName("data");

        //手肘法获取最优K值
        int maxk = columnList.size()/2;
        ElbowMethod elbowMethod = new ElbowMethod();
        int k =elbowMethod.getOptimalK(maxk,testList);
        System.out.println(columnList.size());
        System.out.println(k);

        //执行kmeans算法
        Kmeans kRun = new Kmeans(k,sampleList);
        Set<Cluster> clusterSet = kRun.run();
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

}
