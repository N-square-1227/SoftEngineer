package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
// 用来标注你映射的是哪张表
@TableName("userrole")
public class Userrole implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "userID")
    private Integer userID;
    @TableField("roleID")
    private Integer roleID;

    public Userrole(int roleID) {
        this.roleID = roleID;
    }
}
