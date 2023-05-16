package com.se.softengineer.entity;

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

    /**
     * 子节点
     */
    private List<TreeData> children;

    public TreeData() {

    }

    public TreeData(Indexsym indexsym) {
        this.id = indexsym.getNodeId();
        this.name = indexsym.getNodeName();
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

    public List<TreeData> getChildren() {
        return children;
    }

    public void setChildren(List<TreeData> children) {
        this.children = children;
    }
}
