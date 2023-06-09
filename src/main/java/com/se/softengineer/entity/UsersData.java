package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UsersData {

    @TableId(value = "id",type = IdType.AUTO)
    Integer id;

    @TableField("dataTableName")
    String dataTableName;

    @TableField("indexSymDTName")
    String indexSymDTName;

    @TableField("uploadTime")
    String uploadTime;

}
