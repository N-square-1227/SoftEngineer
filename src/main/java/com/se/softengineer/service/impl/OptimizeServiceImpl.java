package com.se.softengineer.service.impl;

import com.se.softengineer.algorithm.EntropyWeight.Entropy;
import com.se.softengineer.algorithm.Kmeans.Cluster;
import com.se.softengineer.algorithm.Kmeans.ElbowMethod;
import com.se.softengineer.algorithm.Kmeans.Kmeans;
import com.se.softengineer.algorithm.Kmeans.Point;
import com.se.softengineer.algorithm.dataprocess.DataNumpy;
import com.se.softengineer.algorithm.pca.PCA;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.service.OptimizeService;
import com.se.softengineer.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OptimizeServiceImpl implements OptimizeService {

    @Autowired
    private IndexSymService indexSymService;

    @Autowired
    private SampleService sampleService;

    /**
     * @Author 南希诺
     * @create 2023.5.10
     * 熵权法
     * @return
     */
    @Override
    public IndexSym entropy(String indexsym_name, String data_tablename) {
        Entropy entropy = new Entropy();

        //这里的data需要从前端传回来
        List<Sample> data = sampleService.getData(data_tablename);
        List<String> columnList = sampleService.getColName(data_tablename);
        IndexSym newIndexSym = new IndexSym();

        List<List<Double>> dataList = new ArrayList<>();

        DataNumpy.transposition(data);
        for(Sample sample : data) {
            dataList.add(sample.getData());
        }
        /* 处理数据并填充到 entropy 中 */
        // 把二维的数据表传进去
        entropy.setData2D(dataList);
        entropy.setStdY2D(dataList);
        // 指标的个数
        entropy.setIndexNumber(columnList.size());
        // 计算每个指标子指标的个数并填充到 entropy 中

        List<IndexSymNode> node = indexSymService.getIndex(indexsym_name);
        entropy.setNode(node);
        entropy.fillMap(node);

        // 调用算法类的熵权法
        entropy.algorithm();

        String newIndexSymName = indexsym_name + "_new_entropy";
        indexSymService.dropExistTable(newIndexSymName);
        indexSymService.createTable(newIndexSymName);
        // 将新的指标体系存到数据库的新表里
        indexSymService.insertIntoSheet(newIndexSymName, entropy.getNode());
        newIndexSym.setNodeList(entropy.getNode());
        return newIndexSym;
    }

    @Override
    public IndexSym pca(String indexsym_name, String data_tablename) {
        List<Sample> data = sampleService.getData(data_tablename);
        IndexSym indexSym = new IndexSym(indexSymService.getIndex(indexsym_name));
        List<IndexSymNode> leaves = indexSym.get_leaves();
        PCA pca = new PCA(data);
        pca.solve();
        for(int i = 0; i < pca.getFactor_num(); i ++) {
            List<Integer> son_nodes = pca.getNew_sym().getNodeTree().get(pca.getNew_sym().getNodeList().get(i).getNodeID());
            System.out.println(pca.getNew_sym().getNodeList().get(i).getNodeName());
            for (Integer son_node : son_nodes) {
                /* 第idx个叶子节点*/
                int idx = Integer.parseInt(pca.getNew_sym().getNodeList().get(son_node - 1).getNodeName());
                pca.getNew_sym().getNodeList().get(son_node - 1).setNodeName(leaves.get(idx - 1).getNodeName());
            }
        }
        return pca.getNew_sym();
    }

    /**
     * kmeans算法调用
     * @author xly
     * @return
     * @throws Exception
     */
    @Override
    public IndexSym kmeans(String indexsym_name, String data_tablename) throws Exception {
        List<Sample> sampleList,testList = new ArrayList<>();
        List<IndexSymNode> indexList,leafindex = new ArrayList<>();
        IndexSym newIndexSym = new IndexSym();
        //这里的data需要从前端传回来
        sampleList = sampleService.getData(data_tablename);
        testList = sampleService.getData(data_tablename);
        List<String> columnList = new ArrayList<>();
        columnList = sampleService.getColName(data_tablename);
        indexList = indexSymService.getIndex(indexsym_name);
        IndexSym indexSym = new IndexSym(indexList);
        leafindex = indexSym.get_leaves();//获取叶子节点

        //手肘法获取最优K值
        int maxk = columnList.size()/2;
        ElbowMethod elbowMethod = new ElbowMethod();
        int k =elbowMethod.getOptimalK(maxk,testList);
        System.out.println(columnList.size());
        System.out.println(k);

        //执行kmeans算法
        Kmeans kRun = new Kmeans(k,sampleList);
        Set<Cluster> clusterSet = kRun.run();
        List<IndexSymNode> nodeList = new ArrayList<>();
        int centerNum = clusterSet.size();
        for(int i=1;i<=centerNum;i++){
            IndexSymNode node = new IndexSymNode(i,"father"+i,1,1.0,0);
            nodeList.add(node);
        }
        int num = 1;
        for(Cluster cluster:clusterSet){
            List<Point> pointList = cluster.getMembers();
            for(Point point:pointList){
                int id = point.getId();
                IndexSymNode node = new IndexSymNode(centerNum+1,leafindex.get(id).getNodeName(),leafindex.get(id).getNodeType(),leafindex.get(id).getNodeWeight(),num);
                nodeList.add(node);
                centerNum += 1;
            }
            num += 1;
        }
        indexSymService.dropExistTable("xlytest");
        indexSymService.createTable("xlytest");
        // 将新的指标体系存到数据库的新表里
        indexSymService.insertIntoSheet("xlytest", nodeList);
        newIndexSym.setNodeList(nodeList);
        return newIndexSym;
    }
}
