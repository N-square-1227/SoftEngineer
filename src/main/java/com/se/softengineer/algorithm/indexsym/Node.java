package com.se.softengineer.algorithm.indexsym;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 指标体系一个节点的信息
 * 有需要再补充或者再改
 **/
@Data
@TableName("indexsym")
public class Node {

    @TableId(value = "nodeID")
    private Integer node_id;

    @TableField("nodeName")
    private String node_name;

    @TableField("nodeType")
    private Integer node_type;

    @TableField("nodeWeight")
    private Double node_weight;

    @TableField("ParentID")
    // 先暂时用int, 不知道用id比较好还是用整个节点比较好
    private Integer frnode_id;   // father node id

    public Node() {}

    public Node(int id, String name, int type, double weight, int father){
        node_id = id;
        node_name = name;
        node_type = type;
        node_weight = weight;
        frnode_id = father;
    }

    public Integer getNode_id() {
        return node_id;
    }

    public void setNode_id(Integer node_id) {
        this.node_id = node_id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public Integer getNode_type() {
        return node_type;
    }

    public void setNode_type(Integer node_type) {
        this.node_type = node_type;
    }

    public Double getNode_weight() {
        return node_weight;
    }

    public void setNode_weight(Double node_weight) {
        this.node_weight = node_weight;
    }

    public Integer getFrnode_id() {
        return frnode_id;
    }

    public void setFrnode_id(Integer frnode_id) {
        this.frnode_id = frnode_id;
    }
}

