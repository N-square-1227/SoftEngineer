package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author xly
 */
@Data
@TableName("indexsym")
public class Indexsym implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    @TableId(value = "node_id",type = IdType.AUTO)
    private int nodeID;

    @TableField("node_name")
    private String nodeName;

    @TableField("node_type")
    private int nodeType;

    @TableField("node_weight")
    private Double nodeWeight;

    @TableField("parent_id")
    private int ParentID;
}
