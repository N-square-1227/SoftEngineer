package com.se.softengineer.service.impl;

import com.se.softengineer.algorithm.EntropyWeight.Entropy;
import com.se.softengineer.algorithm.Kmeans.Cluster;
import com.se.softengineer.algorithm.Kmeans.ElbowMethod;
import com.se.softengineer.algorithm.Kmeans.Kmeans;
import com.se.softengineer.algorithm.Kmeans.Point;
import com.se.softengineer.algorithm.caculate.CaculateResult;
import com.se.softengineer.algorithm.dataprocess.DataNumpy;
import com.se.softengineer.algorithm.pca.PCA;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.IndexSymResult;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.service.OptimizeService;
import com.se.softengineer.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

        /* 返回新的指标体系 */
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
        int node_num = pca.getNew_sym().getNodeList().size();
        for(int i = 0; i < node_num; i ++) {
            List<Integer> son_nodes = pca.getNew_sym().getNodeTree().get(i + 1);
            if(son_nodes.size() != 0) continue;
            System.out.println(pca.getNew_sym().getNodeList().get(i).getNodeName());
            /* 第idx个子节点*/
            int idx = Integer.parseInt(pca.getNew_sym().getNodeList().get(i).getNodeName());
            pca.getNew_sym().getNodeList().get(i).setNodeName(leaves.get(idx - 1).getNodeName());
        }
        String newIndexSymName = indexsym_name + "_new_pca";

        indexSymService.dropExistTable(newIndexSymName);
        indexSymService.createTable(newIndexSymName);
        // 将新的指标体系存到数据库的新表里
        indexSymService.insertIntoSheet(newIndexSymName, pca.getNew_sym().getNodeList());

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
        IndexSymNode node = new IndexSymNode(1,indexsym_name,1,1.0,0);
        nodeList.add(node);
        for(int i=2;i<=centerNum + 1;i++){
            node = new IndexSymNode(i,"father"+i,1,1.0,1);
            nodeList.add(node);
        }
        int num = 1;
        for(Cluster cluster:clusterSet){
            List<Point> pointList = cluster.getMembers();
            for(Point point:pointList){
                int id = point.getId();
                node = new IndexSymNode(centerNum+1 + 1,leafindex.get(id).getNodeName(),leafindex.get(id).getNodeType(),leafindex.get(id).getNodeWeight(),num + 1);
                nodeList.add(node);
                centerNum += 1;
            }
            num += 1;
        }

        String newIndexSymName = indexsym_name + "_new_kmeans";

        indexSymService.dropExistTable(newIndexSymName);
        indexSymService.createTable(newIndexSymName);
        // 将新的指标体系存到数据库的新表里
        indexSymService.insertIntoSheet(newIndexSymName, nodeList);
        newIndexSym.setNodeList(nodeList);
        return newIndexSym;
    }

    @Override
    public List<IndexSymResult> caculateResult(String dataName, String indexName, String newindexName){
        List<Sample> data = sampleService.getData(dataName);
        IndexSym indexSym = new IndexSym(indexSymService.getIndex(indexName));
        IndexSym newindexSym = new IndexSym(indexSymService.getIndex(newindexName));
        int num = data.size();
//        TreeMap<Double, Integer> map = new TreeMap<>();
        List<IndexSymResult> res_list = new ArrayList<>();

        for(int i = 0; i < num; i ++) {
            CaculateResult res_caculator = new CaculateResult(data.get(i).getData(), indexSym, newindexSym);
            IndexSymResult res = new IndexSymResult(res_caculator.caculateValue(), i);
            res_list.add(res);
        }

        Collections.sort(res_list);

        return res_list;
    }
}
