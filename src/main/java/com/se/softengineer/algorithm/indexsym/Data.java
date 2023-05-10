package com.se.softengineer.algorithm.indexsym;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储用户导入的数据
 * 应该具有普适性
 * 后面看需要再补充
 **/
public class Data {
    /*
     * 有一个方案是用map存储字段名和数据
     * 也就是List<Map<String, List<Double>>
     * Map<String,List<Double>>里是每个字段对应的所有值
     * 最外面的List是字段序列
     * resultset还可以，但是自动装配什么的恐怕不太好做（如果可以更好）
     * ！！！所以直接List<List<Double>>, 认为字段的顺序就是在指标体系那个表里子节点出现的顺序！！！希望能找到更好的解决方式
     * 没有用二维数组，因为想着List不限制长度，Java的二维数组我不太知道是什么个路数
     **/
    private List<List<Double>> data;

    private Integer index_num;
    private Integer sample_num;

    public Data() {
        data = new ArrayList<>();
        index_num = 0;
        sample_num = 0;
    }

    public Data(List<List<Double>> data) {
        this.data = data;
        /**
         * 初始化的时候index_num 和 sample_num未必正确
         * 所以需要做转置，转置之后就是对应的了
         **/
        this.index_num = data.size();
        try {
            this.sample_num = data.get(0).size();
        }
        catch (IndexOutOfBoundsException e) {
            this.index_num = 0;
            this.sample_num = 0;
        }
    }

    public void data_clear() {
        data.clear();
    }

    public List<List<Double>> getData() {
        return data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
        setIndex_num(data.size());
        try {
            setSample_num(data.get(0).size());
        }
        catch (IndexOutOfBoundsException e) {
            setIndex_num(0);
            setSample_num(0);
        }
    }

    public Integer getIndex_num() {
        return index_num;
    }

    public void setIndex_num(Integer index_num) {
        this.index_num = index_num;
    }

    public Integer getSample_num() {
        return sample_num;
    }

    public void setSample_num(Integer sample_num) {
        this.sample_num = sample_num;
    }
}
