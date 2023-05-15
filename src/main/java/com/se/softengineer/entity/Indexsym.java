package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指标体系一个节点的信息
 * 有需要再补充或者再改
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("indexsym")
public class Indexsym {

    @TableId(value = "node_id")
    private Integer nodeId;

    @TableField("node_name")
    private String nodeName;

    @TableField("node_type")
    private Integer nodeType;

    @TableField("node_weight")
    private Double nodeWeight;

    @TableField("parent_id")
    // 先暂时用 int, 不知道用 id 比较好还是用整个节点比较好
    private Integer parentID;
}


