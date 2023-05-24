package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 指标体系一个节点的信息
 * 有需要再补充或者再改
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("indexsym")
@EqualsAndHashCode(callSuper = false)
public class IndexSymNode implements Serializable, Comparable<IndexSymNode>{

    @Serial
    private static final long serialVersionUID = 2L;

    @TableId(value = "node_id")
    private Integer nodeID;

    @TableField("node_name")
    private String nodeName;

    @TableField("node_type")
    private Integer nodeType;

    @TableField("node_weight")
    private Double nodeWeight;

    @TableField("parent_id")
    // 先暂时用 int, 不知道用 id 比较好还是用整个节点比较好
    private Integer parentID;

    private List<IndexSymNode> children = new ArrayList<>();

    public IndexSymNode(int nodeId, String nodeName, int nodeType, double nodeWeight, int parentID) {
        this.nodeID = nodeId;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.nodeWeight = nodeWeight;
        this.parentID = parentID;
    }

    @Override
    //如果该点到原点的距离大于o点到原点的距离，则该点大于o点
    public int compareTo(IndexSymNode o) {
        return this.nodeWeight.compareTo(o.nodeWeight);
    }



}


