package com.se.softengineer.algorithm;

import com.se.softengineer.algorithm.indexsym.Data;
import com.se.softengineer.algorithm.indexsym.IndexSym;
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

        /* 把数据做pca */
        PCA pca = new PCA(data);
        /* 最后的结果比如什么协方差矩阵啦特征向量啦分类情况啦打算写到PCA类的属性*/
        pca.solve();/* 这个函数还没写 */
    }

}
