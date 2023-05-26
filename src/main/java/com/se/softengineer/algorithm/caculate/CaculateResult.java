package com.se.softengineer.algorithm.caculate;

import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import lombok.Data;

import java.util.*;

@Data
public class CaculateResult {
    private List<Double> data;        //用于对比优化后的体系和优化前的体系的数据
    private IndexSym indexSym;      //优化前的指标体系
    private IndexSym newIndexSym;   // 优化后的指标体系

    private Map<Integer,Integer> nodeMap;       //新旧指标体系的节点对应

    private List<Integer>  visit;       // 记忆化搜索(没实现)

    private Map<Integer, Integer> id_idx_map;

    public CaculateResult(List<Double> data, IndexSym indexSym, IndexSym newIndexSym) {
        this.data = data;
        this.indexSym = indexSym;
        this.newIndexSym = newIndexSym;
        visit = new ArrayList<>();
        nodeMap = new HashMap<>();
        id_idx_map = new HashMap<>();
        //原来指标体系的叶子节点
        List<IndexSymNode> leafNode = this.indexSym.get_leaves();
        int leaf_num = indexSym.getLeaf_num();
        //新指标体系的叶子节点
        List<IndexSymNode> newleafNode = newIndexSym.get_leaves();
//        System.out.println(newleafNode);
        //对应新旧指标体系叶子节点的值

        for(int i = 0; i < leaf_num; i ++)
            for(IndexSymNode newNode:newleafNode)
                if(leafNode.get(i).getNodeName().equals(newNode.getNodeName())){
                    /* 新指标体系中的叶子节点的id对应原本的指标中的叶子节点索引 */
                    this.nodeMap.put(newNode.getNodeID(),i);
                    break;
                }
//        System.out.println(nodeMap);

        map_id_idx();
    }

    public void map_id_idx() {
        int nodeNum = newIndexSym.getNodeList().size();
        for (int i = 0; i < nodeNum; i++) {
            id_idx_map.put(newIndexSym.getNodeList().get(i).getNodeID(), i);
        }
    }

    public double caculateValue(){
        List<IndexSymNode> nodes = newIndexSym.getNodeList();
        double result = 0.0;
        for(IndexSymNode node:nodes){
            result += caculate_value(node);
        }
        return  result;
    }

    public double caculate_value(IndexSymNode indexSymNode){
        List<Integer> sonNode = newIndexSym.getNodeTree().get(indexSymNode.getNodeID());

        if(sonNode.size() == 0){
//            System.out.println(indexSymNode);
            return indexSymNode.getNodeWeight() * data.get(nodeMap.get(indexSymNode.getNodeID()));
        }

        double result = 0.0;
        /* 子节点的id */
        for(int i : sonNode) {
            /* 这里既然这样根据nodeid从nodeList里获取节点，意味着限制节点id必须从1开始 */
            /* v2.0 添加了节点和下标的映射, 其他的地方忘了，最起码这里应该是可以节点id任意了，只要不重复就可以 */
            result += caculate_value(newIndexSym.getNodeList().get(id_idx_map.get(i))) * indexSymNode.getNodeWeight();
        }
        return result * indexSymNode.getNodeWeight();
    }
}
