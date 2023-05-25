package com.se.softengineer.algorithm.caculate;

import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

public class CaculateSample {
    private IndexSym indexSym;
    private Sample sample;

    private Boolean[] visit;

    private Double[] res_array;

    @Data
    class Result implements Serializable {
        Map<Integer, Double> res_map = new HashMap<>();
        List<Double> res = new ArrayList<>();
    }


    public CaculateSample() {}

    public CaculateSample(IndexSym indexSym, Sample sample) {
        this.indexSym = indexSym;
        this.sample = sample;
        visit = new Boolean[indexSym.getNodeList().size() + 3]; // 初始化时全部为false
        Arrays.fill(visit, false);
        res_array = new Double[indexSym.getNodeList().size() + 3];
        Arrays.fill(res_array, 0.0);
//        System.out.println(visit.length);
    }

    /*
     * 根据一个指标体系和一组数据，计算指标体系中各节点的值
     * 返回值为List<Double> 对应indexSystem中nodeList的值
     * indexSym中包含根节点并且根节点是第一个（id为1，索引为0）
     **/
    public Map<Integer, Double> caculate() {
        List<Double> res_list = new ArrayList<>();

        List<IndexSymNode> nodes = indexSym.getNodeList();
        List<IndexSymNode> leaves = indexSym.get_leaves();
        int nodeNum = nodes.size();
        int leafNum = leaves.size();

        /* key： 节点id； value：叶子节点索引，即在数据集中的索引 */
        Map<Integer, Integer> leaf_map = new HashMap<>();

        /* 初始化 */
        for(int i = 0; i < nodeNum; i ++)
            res_list.add(0.0);

        /* 建立叶子节点id和数据索引的映射 */
        for(int i = 0; i < leafNum; i ++) {
            leaf_map.put(leaves.get(i).getNodeID(), i);
        }

        for(int i = 0; i < nodeNum; i ++) {
            res_list.set(i, caculateRecursion(nodes.get(i), leaf_map));
        }

        /* 将节点值与节点id映射 */
        Map<Integer, Double> res_map = new HashMap<>();
        for(int i = 0; i < nodeNum; i ++)
            res_map.put(nodes.get(i).getNodeID(), res_list.get(i));

        Result res = new Result();
        res.res = res_list;
        res.res_map = res_map;
        return res.res_map;
    }

    public double caculateRecursion(IndexSymNode node, Map<Integer, Integer> leaf_map) {
        List<Integer> sons = indexSym.getNodeTree().get(node.getNodeID());
//        System.out.println(node.getNodeID());

        if(sons.size() == 0) {
            visit[node.getNodeID()] = true;
            res_array[node.getNodeID()] = node.getNodeWeight() * sample.getData().get(leaf_map.get(node.getNodeID()));
            return res_array[node.getNodeID()];
        }

        if(visit[node.getNodeID()]) {
            return res_array[node.getNodeID()];
        }

        double result = 0.0;
        /* 子节点的id */
        for(int i : sons) {
            /* 这里既然这样根据nodeid从nodeList里获取节点，意味着限制节点id必须从1开始 */
            result += caculateRecursion(indexSym.getNodeList().get(i - 1), leaf_map);
        }
        visit[node.getNodeID()] = true;
        res_array[node.getNodeID()] = result * node.getNodeWeight();
        return res_array[node.getNodeID()];
    }
}
