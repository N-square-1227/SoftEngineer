package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lmy
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    /**
     * 菜单编码
     */
    @TableField("menuCode")
    private String menuCode;

    /**
     * 菜单名字
     */
    @TableField("menuName")
    private String menuName;

    /**
     * 点击触发的函数
     */
    @TableField("menuClick")
    private String menuClick;

    /**
     * 权限 1管理员，2表示用户
     */
    @TableField("menuRight")
    private Integer menuRight;

    /**
     * 菜单页面的路径
     */
    @TableField("menuComponent")
    private String menuComponent;

    /**
     * 菜单图标
     */
    @TableField("menuIcon")
    private String menuIcon;


}
