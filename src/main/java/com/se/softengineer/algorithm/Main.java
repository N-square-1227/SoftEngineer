package com.se.softengineer.algorithm;

import com.se.softengineer.algorithm.indexsym.Data;
import com.se.softengineer.algorithm.indexsym.IndexSym;
import com.se.softengineer.algorithm.indexsym.Node;
import com.se.softengineer.algorithm.pca.PCA;
import com.se.softengineer.algorithm.trydatabase.TestMySQL;

import java.util.List;

/**
 * wxy用来测试的主类
 * 没运行过也没写完（没有运行就没有bug）
 * 要用自取
 **/
public class Main {

    public static void main(String[] args) throws Exception {
        /* 获取数据 */
        TestMySQL sql = new TestMySQL();
        Data data = new Data(sql.queryData("data"));
//        System.out.println(data.getData());
        IndexSym indexSym = new IndexSym(sql.querySym("indexsym"));
        List<Node> leaves = indexSym.get_leaves();

        /* 把数据做pca */
        PCA pca = new PCA(data);
        /* 最后的结果比如什么协方差矩阵啦特征向量啦分类情况啦打算写到PCA类的属性*/
        pca.solve();
        /* pca里的名字是叶子节点序号，替换成对应的指标名字 */
        /* 输出 */
        for(int i = 0; i < pca.getFactor_num(); i ++) {
            List<Integer> son_nodes = pca.getNew_sym().getNodeTree().get(pca.getNew_sym().getNodeList().get(i).getNode_id());
            System.out.println(pca.getNew_sym().getNodeList().get(i).getNode_name());
            for (Integer son_node : son_nodes) {
                /* 第idx个叶子节点*/
                int idx = Integer.parseInt(pca.getNew_sym().getNodeList().get(son_node - 1).getNode_name());
                pca.getNew_sym().getNodeList().get(son_node - 1).setNode_name(leaves.get(idx - 1).getNode_name());
                System.out.println("\t" + pca.getNew_sym().getNodeList().get(son_node - 1).getNode_name());
            }
        }


    }

}
