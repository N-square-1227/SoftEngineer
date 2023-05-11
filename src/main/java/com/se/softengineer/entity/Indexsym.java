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
    @TableId(value = "nodeID",type = IdType.AUTO)
    private int nodeID;

    @TableField("nodeName")
    private String nodeName;

    @TableField("nodeType")
    private int nodeType;

    @TableField("nodeWeight")
    private Double nodeWeight;

    @TableField("ParentID")
    private int ParentID;
}
