package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author 南希诺
 * @create 2023.5.10
 */
@Data
@TableName("data")
public class data {
    @TableId(value = "No", type = IdType.AUTO)
    private Double No;
    private Double X1;
    private Double X2;
    private Double X3;
    private Double X4;
    private Double X5;
    private Double X6;
    private Double X7;
    private Double X8;
    private Double X9;
    private Double X10;
    private Double X11;
    private Double X12;
    private Double X13;
    private Double X14;
    private Double X15;
    private Double X16;
    private Double X17;
    private Double X18;
    private Double X19;
    private Double X20;
    private Double X21;
    private Double X22;
    private Double X23;
}