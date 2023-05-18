package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class TreeData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private int id;

    /**
     * 节点名称
     */
    private String name;

    private Integer type;

    private Double weight;

    private Integer parentID;

    /**
     * 子节点
     */
    private List<TreeData> children;

    public TreeData() {

    }

    public TreeData(IndexSymNode indexsym) {
        this.id = indexsym.getNodeID();
        this.name = indexsym.getNodeName();
        this.type = indexsym.getNodeType();
        this.weight = indexsym.getNodeWeight();
        this.parentID = indexsym.getParentID();
        this.children = indexsym.getChildren().stream().map(TreeData::new).collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public List<TreeData> getChildren() {
        return children;
    }

    public void setChildren(List<TreeData> children) {
        this.children = children;
    }
}
