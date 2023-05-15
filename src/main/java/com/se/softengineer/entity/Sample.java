package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Sample {

    //    @TableId(value = "id",type= IdType.AUTO)
//    Integer id;
    @SuppressWarnings("unchecked")
    @TableField("data")
    List data = new ArrayList<>();

    public Sample() {}

    public List<Double> getData() {
//        System.out.println(data);
        return data;
    }

    public void setData(List<Double> dataList) {
        data = dataList;
    }
}