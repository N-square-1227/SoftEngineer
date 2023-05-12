package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * by wxy
 * 试试springboot
 **/
@Data
@EqualsAndHashCode(callSuper = false)
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
    private Integer parentID;

    
}

