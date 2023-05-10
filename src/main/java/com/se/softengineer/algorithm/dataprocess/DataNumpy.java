package com.se.softengineer.algorithm.dataprocess;

import com.se.softengineer.algorithm.indexsym.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 还没写完私密马赛
 * 处理数据的类，定义的都是static的方法
 * 做一些类似求方差啦求平均值啦之类的事情
 */
public class DataNumpy {

    /* 从数据库里读来的数据List<List>里面一个List是一个样本
     * 想改成里面的一个List一个指标方便处理
     **/
    public static void transposition(Data data) {
        // Java里没有很明显的方法区分值传参还是引用传参
        // 但是据说自定义Object的传参是引用传参, 所以试一下
        int index_num = data.getIndex_num();
        int sample_num = data.getSample_num();

        /* 二维数组比较好还是List比较好我也不知道 */
        Double data_matrix[][] = new Double[sample_num][index_num];

        /* 先放到二维数组里存起来，二维数据方便就方便在可以下标遍历 */
        for(int i = 0; i < index_num; i ++)
            for(int j = 0; j < sample_num; j ++)
                data_matrix[i][j] = data.getData().get(i).get(j);

        data.data_clear();

        List<List<Double>> new_data = new ArrayList<>();
        data.setData(new_data);

        /* 把data的List<List> 变成里面的List是指标，外面的List是样本 */
        for(int j = 0; j < sample_num; j ++) {
            List<Double> sample = new ArrayList<>();
            for(int i = 0; i < index_num; i ++){
                sample.add(data_matrix[i][j]);
            }
        }
        /**
         * 意思就是这个意思，流程就是先把原来的数据存到二维数组里（方便行列转换直接取下标）
         * 然后把原本的清空
         * 转置之后再把新的数据set到对象里
         *
         * 问题在于：
         * 1. 希望的是调用这个函数的时候，Data.transposition(data)，这一行运行完之后data里的List<List>就被转置了
         * 2. 原本的List<List>被清空了, 不知道是不是保存了capacity信息，可能会有点问题
         */
    }
}

