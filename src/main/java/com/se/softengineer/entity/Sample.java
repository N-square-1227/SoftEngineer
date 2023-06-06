package com.se.softengineer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Sample implements Serializable {

    //    @TableId(value = "id",type= IdType.AUTO)
//    Integer id;
    @SuppressWarnings("unchecked")
    @TableField("data")
    List data = new ArrayList<>();

    public Sample() {}

    public Sample(List<Double> data){
        this.data = data;
    }

    public List<Double> getData() {
//        System.out.println(data);
        return data;
    }

    public void setData(List<Double> dataList) {
        data = dataList;
    }
}
