package com.se.softengineer.algorithm.algorithmResult;

import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.service.SampleService;

import java.util.List;
import java.util.Map;

public class CalulateResult {
    private List<Double> data;        //用于对比优化后的体系和优化前的体系的数据
    private IndexSym indexSym;      //优化前的指标体系
    private IndexSym newIndexSym;   // 优化后的指标体系
    private List<IndexSymNode> nodeList;        //优化前的指标体系
    private List<IndexSymNode> newnodeList;     //优化后的指标体系
    private Map<Integer,Integer> nodeMap;       //新旧指标体系的节点对应

    private SampleService sampleService;
    private IndexSymService indexSymService;

    public CalulateResult(List<Double> data, IndexSym indexSym, IndexSym newIndexSym) {
        this.data = data;
        this.indexSym = indexSym;
        this.newIndexSym = newIndexSym;
    }

    private void InitData(){
        //原来指标体系的叶子节点
        IndexSym indexSym = new IndexSym(this.nodeList);
        List<IndexSymNode> leafNode = indexSym.get_leaves();
        int leaf_num = indexSym.getLeaf_num();
        //新指标体系的叶子节点
        IndexSym newindexSym = new IndexSym(this.newnodeList);
        List<IndexSymNode> newleafNode = newindexSym.get_leaves();
        //对应新旧指标体系叶子节点的值

        for(int i = 0; i < leaf_num; i ++){
            for(IndexSymNode newNode:newleafNode){
                if(leafNode.get(i).getNodeName() == newNode.getNodeName()){
                    this.nodeMap.put(newNode.getNodeID(),i);
                    break;
                }
            }
        }
    }

    public double caculateValue(){
        InitData();
        List<IndexSymNode> nodes = newIndexSym.getNodeList();
        double result = 0.0;
        for(IndexSymNode node:nodes){
            result += caculate_value(node);
        }
        return  result;
    }

    public double caculate_value(IndexSymNode indexSymNode){
        List<Integer> sonNode = indexSym.getNodeTree().get(indexSymNode.getNodeID());

        if(sonNode.size() == 0){
            return indexSymNode.getNodeWeight() * data.get(nodeMap.get(indexSymNode.getNodeID()));
        }

        double result = 0.0;
        for(int i : sonNode) {
            result += caculate_value(indexSym.get_leaves().get(i - 1)) * indexSymNode.getNodeWeight();
        }
        return result * indexSymNode.getNodeWeight();
    }
}
