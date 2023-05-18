package com.se.softengineer.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * by wxy
 * 试试springboot
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class IndexSymNode implements Serializable, Comparable<IndexSymNode> {

    @Serial
    private static final long serialVersionUID = 2L;
    @TableId(value = "node_id",type = IdType.AUTO)
    private int nodeID;

    @TableField(value = "node_name")
    private String nodeName;

    @TableField("node_type")
    private int nodeType;

    @TableField("node_weight")
    private Double nodeWeight;

    @TableField("parent_id")

    private List<IndexSymNode> children = new ArrayList<>();

    private int ParentID;

    public IndexSymNode() {}

    public IndexSymNode(int nodeId, String nodeName, int nodeType, double nodeWeight, int parentID) {
        this.nodeID = nodeId;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.nodeWeight = nodeWeight;
        this.ParentID = parentID;
    }

    @Override
    //如果该点到原点的距离大于o点到原点的距离，则该点大于o点
    public int compareTo(IndexSymNode o) {
        return (this.nodeWeight > o.nodeWeight) ? 1 : ((this.nodeWeight == o.nodeWeight) ? 0 : -1);
    }

}
