package com.se.softengineer.algorithm.EntropyWeight;

import com.se.softengineer.dao.UsersMapper;
import com.se.softengineer.entity.data;
import lombok.Data;
import org.apache.catalina.mbeans.SparseUserDatabaseMBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 南希诺
 * @create 2023.5.10
 */
@Data
public class Entropy {

    /**
     * 存放原始数据的数组
     */
    List<Double> dataList;

    /**
     * 存放标准化后 Y 值的数组
     */
    List<Double> stdY;
    /**
     * 存放 P 值的数组
     */
    List<Double> pY;
    /**
     * 存放每一个指标信息熵的数组
     */
    List<Double> infoEntropyList;
    /**
     * 每一个指标权重的数组
     */
    List<Double> WList;
    List<Double> SList;
    /**
     * stdYij 的和
     */
    double sumYij;
    /**
     * 熵权数组的总和
     */
    double sumEj;
    /**
     * 权重数组的总和
     */
    double sumWj;
    /**
     * todo: change 指标的个数
     */
    int indexNumber = 23;
    /**
     * todo: change 每一项指标的值的个数
     */
    int idxChild = 22;
    public void algorithm() {
//        System.out.println(dataList);
        // 1.数据标准化
        standardize();
//        System.out.println(stdY);
        // 2.求各指标的信息熵
        infoEntropy();
//         System.out.println(infoEntropyList);
        // 3.通过信息熵计算各指标的权重
        Weight();
//        System.out.println(WList);
        // 4.评分（可选）
        score();
        System.out.println(SList);

    }

    /**
     * 数据标准化
     */
    public void standardize() {
        // 初始化标准化数组，数组的大小就是指标的个数
        stdY = new ArrayList<>(indexNumber * idxChild);
        // 列最大最小值
        double maxXi, minXi;
        // 有几个指标就需要循环几次
        for (int times = 0; times < indexNumber; times++) {
            // 初始化
            maxXi = Integer.MIN_VALUE;
            minXi = Integer.MAX_VALUE;
            // 内循环一个指标的数据个数，求最大值最小值
            for (int i = 0; i < idxChild; i++) {
                // 起始值：times * idxChild，逐个增加 i
                double value = dataList.get(times * idxChild + i);
                maxXi = Math.max(value, maxXi);
                minXi = Math.min(value, minXi);
            }

            // 最小-最大规范化
            for (int i = 0; i < idxChild; i++) {
                double value = dataList.get(times * idxChild + i);
                double Yij = (value - minXi) / (maxXi - minXi);
                stdY.add(Yij);
                sumYij += Yij;
            }
        }
    }

    /**
     * 求各指标的信息熵
     */
    public void infoEntropy() {
        // 初始化 pY
        pY = new ArrayList<>(stdY.size());
        for (Double aDouble : stdY) {
            // todo:这里做了 10 倍放大处理，不然 pY 太小了
            pY.add(10 * aDouble / sumYij);
        }
        // 初始化 EList,有多少个指标就有多少组信息熵
        // 所以该数组的大小是指标的个数
        infoEntropyList = new ArrayList<>(indexNumber);
        double sumPij;
        // 有几个指标就需要循环几次
        for (int times = 0; times < indexNumber; times++) {
            sumPij = 0.0;
            // 内循环一个指标的数据个数，计算总 P 值
            for (int i = 0; i < idxChild; i++) {
                // 起始值：times * idxChild，逐个增加 i
                double value = pY.get(times * idxChild + i);
                sumPij += value == 0 ? 0 : value * Math.log(value);
            }
            double Ej = (-1) * (1 / Math.log(idxChild)) * sumPij;
            infoEntropyList.add(Ej);
            sumEj += Ej;
        }
    }

    /**
     * 求权重
     */
    public void Weight() {
        // 初始化数组
        WList = new ArrayList<>(indexNumber);
        for (Double aDouble : infoEntropyList) {
            // 这里 k 指的是指标个数
            // todo:为显示方便，这里做了 * 10 放大处理
            double Wi = 10 * (1 - aDouble) / (indexNumber - sumEj);
            WList.add(Wi);
            sumWj += Wi;
        }
    }

    /**
     * 评分
     */
    public void score() {
        SList = new ArrayList<>(indexNumber);
        double score = 0.0;
        int j = 0;
        for (int i = 0; i < pY.size(); i++) {
            if (i / idxChild != j) {
                //todo:为方便观察，这里对 score 做了 100 倍放大处理
                SList.add(100 * score);
                score = 0.0;
                j = i / idxChild;
                i -= 1;
            } else {
                score += WList.get(j) * pY.get(i);
            }
        }
        // 最后再把这个指标加进去
        SList.add(100 * score);
    }


}
