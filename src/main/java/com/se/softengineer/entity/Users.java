package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.se.softengineer.utils.EncryptHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

// lombok 注解
@Data
// 用来标注你映射的是哪张表
@TableName("users")
public class Users implements Serializable {
    /*
    1、将对象的内容进行流化。对流化后的对象进行读写操作，也可以在网络之间传输，需要实现 Serializable 接口.

    2、是对象持久化的一种机制，在程序终止后，这些对象仍然存在，可以在程序再次启动之后读取这些对象的值.

    3、只有序列化的对象才可以存储在存储设备上.
    * */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    // @TableId 用来映射表的主键(主键自增策略,type = IdType.AUTO)
    @TableId(value = "userID")
    private Integer userID;

    @TableField(value = "userName", typeHandler = EncryptHandler.class)
    private String userName;

    @TableField(value = "userPassword", typeHandler = EncryptHandler.class)
    private String userPassword;

    @TableField(value = "userEmail", typeHandler = EncryptHandler.class)
    private String userEmail;

    @TableField("role")
    private Integer role;

    @Override
    public String toString() {
        return "Users{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
