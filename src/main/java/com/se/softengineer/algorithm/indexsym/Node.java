package com.se.softengineer.algorithm.indexsym;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 南希诺 修改过
 * 指标体系一个节点的信息
 * 有需要再补充或者再改
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("indexsym")
public class Node {

    @TableId(value = "node_id",type= IdType.AUTO)
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

