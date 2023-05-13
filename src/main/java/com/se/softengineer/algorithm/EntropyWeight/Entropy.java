package com.se.softengineer.algorithm.EntropyWeight;

import com.se.softengineer.algorithm.indexsym.Node;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 南希诺
 * @create 2023.5.10
 * 参考：
 * https://zhuanlan.zhihu.com/p/448815108
 * https://zhuanlan.zhihu.com/p/139224482
 * https://zhuanlan.zhihu.com/p/267259810
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
     * 每个指标自己所有数据标准化后的和
     */
    List<Double> sumYij;
    /**
     * 存放 P 值的数组
     */
    List<Double> pY;
    /**
     * 存放每一个指标信息熵的数组
     */
    List<Double> EList;
    /**
     * 每一个指标权重的数组
     */
    List<Double> WList;
    /**
     * 评分数组
     */
    List<Double> SList;
    /**
     * 每个指标的评分 Map
     */
    Map<String, Double> resultMap;
    /**
     * 排序后的 Map
     */
    LinkedHashMap<String, Double> sortedMap;

    /**
     * 我的新指标体系！
     */
    List<Node> entropyList;
    /**
     * 权重数组的总和
     */
    double sumWj;
    /**
     * 信息熵和
     */
    double sumEj;
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
//         System.out.println(EList);
        // 3.通过信息熵计算各指标的权重
        Weight();
//        System.out.println(WList);
        // 4.评分（可选）
        //score();
//        System.out.println(SList);
        // 5.验证阶段：取前 15 最大的，和 PDF 比较
        sortMap();
        // 6.数据库持久化
        save();
    }

    /**
     * 数据标准化，该步正确
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
            /*
              到这一步，一个指标内的最大值和最小值就算出来了
              已验证
             */
            // 最小-最大规范化
            for (int i = 0; i < idxChild; i++) {
                double value = dataList.get(times * idxChild + i);
                double Yij = (value - minXi) / (maxXi - minXi);
                stdY.add(Yij);
            }
        }
        // 计算 sumYij
        calSumYij();
    }

    /**
     * 计算 sumYij
     */
    public void calSumYij() {
        sumYij = new ArrayList<>(indexNumber);
        double sum = 0.0;
        int j = 0;
        for (int i = 0; i < stdY.size(); i++) {
            // 下一个轮次
            if (i / idxChild != j) {
                sumYij.add(sum);
                sum = 0.0;
                j = i / idxChild;
                i -= 1;
            } else {
                sum += stdY.get(i);
            }
        }
        sumYij.add(sum);

//        // test ok
//        double s = 0.0;
//        for (int i = 0; i < 22; i++) {
//            s += stdY.get(i);
//        }
//        System.out.println(s);
//        s = 0.0;
//        for (int i = 22; i < 44; i++) {
//            s += stdY.get(i);
//        }
//        System.out.println(s);

    }

    /**
     * 求各指标的信息熵
     */
    public void infoEntropy() {
        // 初始化 pY
        pY = new ArrayList<>(stdY.size());
        for (int i = 0; i < stdY.size(); i++) {
            double sum = sumYij.get(i / idxChild);
            pY.add(stdY.get(i) / sum);
        }
        // 初始化 EList,有多少个指标就有多少组信息熵
        // 所以该数组的大小是指标的个数
        EList = new ArrayList<>(indexNumber);
        double sum = 0.0;
        int j = 0;
        for (int i = 0; i < pY.size(); i++) {
            // 下一个轮次
            if (i / idxChild != j) {
                double Ej = (-1) * (1 / Math.log(idxChild)) * sum;
                EList.add(Ej);
                sumEj += Ej;
                sum = 0.0;
                j = i / idxChild;
                i -= 1;
            } else {
                double value = pY.get(i);
                sum += (value == 0 ? 0 : value * Math.log(value));
            }
        }
        double Ej = (-1) * (1 / Math.log(idxChild)) * sum;
        EList.add(Ej);
        sumEj += Ej;
    }


    /**
     * 求权重
     */
    public void Weight() {
        // 初始化数组
        WList = new ArrayList<>(indexNumber);
        resultMap = new HashMap<>();
        int i = 1;
        for (Double aDouble : EList) {
            // 这里 k 指的是指标个数
            double Wi = (1 - aDouble) / (indexNumber - sumEj);
            WList.add(Wi);

            resultMap.put("X" + i, Wi);
            i++;

            sumWj += Wi;
        }

//        double sum = 0.0;
//        for (int j = 0; j < WList.size(); j++) {
//            sum += WList.get(j);
//            if (j == 6) {
//                resultMap.put("第一类指标", sum);
//                sum = 0.0;
//            } else if (j == 16) {
//                resultMap.put("第二类指标", sum);
//                sum = 0.0;
//            } else if (j == 22) {
//                resultMap.put("第三类指标", sum);
//                sum = 0.0;
//            }
//        }
    }

    /**
     * 评分（depressed）
     */
    public void score() {
        SList = new ArrayList<>(indexNumber);
        resultMap = new HashMap<>();
        double score = 0.0;
        int j = 0;
        for (int i = 0; i < pY.size(); i++) {
            // 是下一个指标的数据了。
            if (i / idxChild != j) {
                SList.add(score);
                resultMap.put("X" + (j + 1), score);
                score = 0.0;
                j = i / idxChild;
                i -= 1;
            } else {
                score += WList.get(j) * pY.get(i);
            }
        }
        // 最后再把这个指标加进去
        SList.add(score);
        resultMap.put("X" + (j + 1), score);
    }

    /**
     * 根据评分对指标进行排序
     */
    public void sortMap() {
        // 将 Map 转换成 List 集合，每个元素是一个 Map.Entry 对象
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(resultMap.entrySet());

        // 使用 Stream API 对 List 集合进行排序，排序方式为按照 Entry 对象的值从高到低排序
        entryList = entryList.stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());

        // 将排好序的 Entry 对象重新存放到一个 LinkedHashMap 中，以保证排序后的顺序不被打乱
        sortedMap = new LinkedHashMap<>();
        entryList.forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));

        // 打印排序后的 Map
//        System.out.println(sortedMap);
    }

    /**
     * 数据库持久化，我这里就直接拿原数组改了
     */
    public void save() {
        // todo：一级指标个数，到时候修改这个值
        int oneIndexNum = 3;
        for (int i = 0; i < WList.size(); i++) {
            entropyList.get(i + oneIndexNum).setNodeWeight(WList.get(i));
        }
        // System.out.println(entropyList);
        // 算 3 个一级指标的权重,左闭右开！！！
        int begin = 3, end = 10;
        int j = 0;
        entropyList.get(j).setNodeWeight(0.0);
        for (int i = begin; i < end; i++) {
            entropyList.get(j).setNodeWeight(entropyList.get(j).getNodeWeight() +
                    entropyList.get(i).getNodeWeight());
        }

        j = j + 1;
        begin = 10; end = 20;
        entropyList.get(j).setNodeWeight(0.0);
        for (int i = begin; i < end; i++) {
            entropyList.get(j).setNodeWeight(entropyList.get(j).getNodeWeight() +
                    entropyList.get(i).getNodeWeight());
        }

        j = j + 1;
        begin = 20; end = 26;
        entropyList.get(j).setNodeWeight(0.0);
        for (int i = begin; i < end; i++) {
            entropyList.get(j).setNodeWeight(entropyList.get(j).getNodeWeight() +
                    entropyList.get(i).getNodeWeight());
        }
    }
}
