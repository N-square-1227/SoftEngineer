package com.se.softengineer.entity;

import com.se.softengineer.entity.IndexSymNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存指标体系的信息
 * 后面不合适再改
 * 有需要再补充
 **/
public class IndexSym {
    /**
     * 应该是指标体系读进来结点的顺序就是List里的顺序
     * 然后Map里只记了在List里的序号
     **/
    // 指标体系中所有指标的信息
    private List<IndexSymNode> nodeList;
    // 指标之间的父子关系
    private Map<Integer, List<Integer>> nodeTree;

    // 叶子节点个数
    private int leaf_num;

    // 无参构造函数
    public IndexSym() {
        nodeList = new ArrayList<>();
        nodeTree = new HashMap<>();
    }

    // 使用节点列表构造
    public IndexSym(List<IndexSymNode> nodes) {
        nodeList = nodes;
        nodeTree = new HashMap<>();
        int node_num = nodeList.size();
        for(int i = 0; i < node_num; i ++) {
            insert2tree(nodes.get(i));
        }
    }

    // 加入节点的两种方式
    // 1. 直接加入一个Node实例
    public void addNode(IndexSymNode node) {
        nodeList.add(node);
        insert2tree(node);
    }


    // 添加节点的父子关系
    public void insert2tree(IndexSymNode node) {
        Integer father_id = node.getParentID();
        if(nodeTree.containsKey(father_id)) {
            /* 理论上数据库如果是id字段自增，拿进来List的下标应该是对应的(List从0开始, 数据库的从1开始)
             * 不是的话find应该也挺好find
             **/
            nodeTree.get(father_id).add(node.getNodeId());
        }
        else {
            List<Integer> temp = new ArrayList<>();
            temp.add(node.getNodeId());
            nodeTree.put(father_id, temp);
        }
        nodeTree.put(node.getNodeId(), new ArrayList<>());
    }

    public List<IndexSymNode> get_leaves() {
        List<IndexSymNode> leaves = new ArrayList<>();

        /* 认为在nodeList里的顺序就是在数据表中存测数据 */
        for(IndexSymNode node : nodeList) {
//            System.out.println(node.getNode_id());
            if(nodeTree.get(node.getNodeId()).size() == 0)
                leaves.add(node);
        }
        leaf_num = leaves.size();
        return leaves;
    }


    public List<IndexSymNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<IndexSymNode> nodeList) {
        this.nodeList = nodeList;
        nodeTree.clear();
        nodeTree = new HashMap<>();
        int node_num = nodeList.size();
        for(int i = 0; i < node_num; i ++) {
            insert2tree(nodeList.get(i));
        }
    }

    public Map<Integer, List<Integer>> getNodeTree() {
        return nodeTree;
    }

    public void setNodeTree(Map<Integer, List<Integer>> nodeTree) {
        this.nodeTree = nodeTree;
    }

    public int getLeaf_num() {
        return leaf_num;
    }

    public void setLeaf_num(int leaf_num) {
        this.leaf_num = leaf_num;
    }
}