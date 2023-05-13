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
     * 指标的正负性
     */
    static int POSITIVE = 1;
    static int NEGATIVE = 0;
    /**
     * 存放原始数据的数组
     */
    List<Double> dataList;

    /**
     * 二维格式的 data
     */
    List<List<Double>> data2D;

    /**
     * 父节点，以该节点为父节点的指标数目
     */
    Map<Integer, Integer> map;

    /**
     * 存放标准化后 Y 值的数组
     */
    List<Double> stdY;
    /**
     * node
     */
    List<Node> node;


    /**
     * 存放标准化后 Y 值的数组
     */
    List<List<Double>> stdY2D;


    /**
     * 每个指标自己所有数据标准化后的和
     */
    List<Double> sumYij;
    /**
     * 存放 P 值的数组
     */
    List<Double> pY;
    /**
     * 存放 P 值的数组
     */
    List<List<Double>> pY2D;
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
     * parentId 集合
     */
    Set<Integer> pIdList;
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
     * 指标的个数
     */
    int indexNumber;
    /**
     * 每一项指标的值的个数
     */
    int idxChild;
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
        // sortMap();
        // 6.数据库持久化
        save();
    }

    /**
     * 遍历 node 数组，填充 map
     */
    public void fillMap(List<Node> node) {
        map = new HashMap<>();
        // 如果map中已经存在key，则更新value值为原来的值加1
        // 否则将value值初始化为1
        for (Node value : node) {
            map.compute(value.getParentID(), (k, v) -> (v == null) ? 1 : v + 1);
        }
        // 输出结果
//        System.out.println(map);
    }
    /**
     * 数据标准化
     */
    public void standardize() {
        // 求实际元素的个数
        int count = 0;
        for (List<Double> subList : data2D) {
            count += subList.size();
        }
        // 初始化标准化数组，数组的大小就是指标的个数
        stdY = new ArrayList<>(count);
        // 一个指标内的最大最小值
        double maxXi, minXi;
        for (int i = 0; i < data2D.size(); i++) {
            // 初始化
            maxXi = Integer.MIN_VALUE;
            minXi = Integer.MAX_VALUE;
            for (int j = 0; j < data2D.get(i).size(); j++) {
                double xValue = data2D.get(i).get(j);
                maxXi = Math.max(xValue, maxXi);
                minXi = Math.min(xValue, minXi);
            }

            /*
              到这一步，一个指标内的最大值和最小值就算出来了
             */
            // 最小-最大规范化
            int diff = node.size() - data2D.size();
            double Yij;
            for (int j= 0; j < data2D.get(i).size(); j++) {
                double xValue = data2D.get(i).get(j);
                // 正向指标
                if (node.get(i + diff).getNodeType() == POSITIVE) {
                    Yij = (xValue - minXi) / (maxXi - minXi);
                } else { // 负向指标
                    Yij = (maxXi - xValue) / (maxXi - minXi);
                }
                stdY2D.get(i).set(j, Yij);
            }
        }
        // 计算 sumYij
        calSumYij();
    }

    /**
     * 计算每个指标的 sumYij
     */
    public void calSumYij() {
        sumYij = new ArrayList<>(indexNumber);
        double sum = 0.0;

        for (List<Double> doubles : stdY2D) {
            for (Double aDouble : doubles) {
                sum += aDouble;
            }
            sumYij.add(sum);
            sum = 0.0;
        }
    }

    /**
     * 求各指标的信息熵
     */
    public void infoEntropy() {
        // 用是 stdY2D 初始化 pY2D
        pY2D = stdY2D;
        for (int i = 0; i < pY2D.size(); i++) {
            for (int j = 0; j < pY2D.get(i).size(); j++) {
                double Pij = stdY2D.get(i).get(j) / sumYij.get(i);
                pY2D.get(i).set(j, Pij);
            }
        }
        // 初始化 EList,有多少个指标就有多少组信息熵
        // 所以该数组的大小是指标的个数
        EList = new ArrayList<>(indexNumber);
        double sum = 0.0;
        for (List<Double> doubles : pY2D) {
            for (double value : doubles) {
                if (value == 0.0) {
                    sum = 0.0;
                } else {
                    sum += value * Math.log(value);
                }
            }
            double Ej = (-1) * (1 / Math.log(doubles.size())) * sum;
            EList.add(Ej);
            sumEj += Ej;
            sum = 0.0;
        }
    }


    /**
     * 求权重
     */
    public void Weight() {
        // 初始化数组
        WList = new ArrayList<>(indexNumber);
        for (Double aDouble : EList) {
            // 这里 k 指的是指标个数
            double Wj = (1 - aDouble) / (indexNumber - sumEj);
            WList.add(Wj);
            sumWj += Wj;
        }
//
//        // 初始化数组
//        WList = new ArrayList<>(indexNumber);
//        resultMap = new HashMap<>();
//        int i = 1;
//        for (Double aDouble : EList) {
//            // 这里 k 指的是指标个数
//            double Wi = (1 - aDouble) / (indexNumber - sumEj);
//            WList.add(Wi);
//
//            resultMap.put("X" + i, Wi);
//            i++;
//
//            sumWj += Wi;
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
     * 生成新的数组
     */
    public void save() {
        int diff = node.size() - WList.size();
        // 赋值权重，注意是倒序
        for (int i = node.size() - 1; i >= diff; i--) {
            node.get(i).setNodeWeight(WList.get(i - diff));
        }
        // 排序
        Collections.sort(node);
        // 倒序删除部分节点
        double sum = node.get(0).getNodeWeight();
        int num = 0;
        for (int i = 1; i < node.size() - 1; i++) {
            // 权重有一定意义了,or...
            if (sum >= node.get(i).getNodeWeight() || num >= node.size() * 0.3) {
                break;
            } else {
                num += 1;
                sum += node.get(i).getNodeWeight();
            }
        }

        // del
        while (num > 0) {
            node.remove(0);
            int key = node.get(0).getParentID();
            map.put(key, map.get(key) - 1);
            num--;
        }

        // 求各级指标的权重
        // map:"{0=3,1=6,2=10,3=6}"
        Map<Integer, Double> weight = new HashMap<>();
        for (Node value : node) {
            weight.compute(value.getParentID(), (k, v) -> (v == null) ? value.getNodeWeight() : v + value.getNodeWeight());
        }
        for (int key : map.keySet()) {
            for (Node value : node) {
                if (value.getNodeId() == key) {
                    value.setNodeWeight(weight.get(key));
                }
            }
        }
//        System.out.println("hello");
//        // todo：一级指标个数，到时候修改这个值
//        int oneIndexNum = 3;
//        for (int i = 0; i < WList.size(); i++) {
//            node.get(i + oneIndexNum).setNodeWeight(WList.get(i));
//        }
//        // System.out.println(node);
//        // 算 3 个一级指标的权重,左闭右开！！！
//        int begin = 3, end = 10;
//        int j = 0;
//        node.get(j).setNodeWeight(0.0);
//        for (int i = begin; i < end; i++) {
//            node.get(j).setNodeWeight(node.get(j).getNodeWeight() +
//                    node.get(i).getNodeWeight());
//        }
//
//        j = j + 1;
//        begin = 10; end = 20;
//        node.get(j).setNodeWeight(0.0);
//        for (int i = begin; i < end; i++) {
//            node.get(j).setNodeWeight(node.get(j).getNodeWeight() +
//                    node.get(i).getNodeWeight());
//        }
//
//        j = j + 1;
//        begin = 20; end = 26;
//        node.get(j).setNodeWeight(0.0);
//        for (int i = begin; i < end; i++) {
//            node.get(j).setNodeWeight(node.get(j).getNodeWeight() +
//                    node.get(i).getNodeWeight());
//        }


    }
}
