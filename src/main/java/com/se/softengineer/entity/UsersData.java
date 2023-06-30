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

//    这个字段是不是实际没用到
//    可恶忘记了，这我亲手加的
    @TableField("time")
    String time;

}
