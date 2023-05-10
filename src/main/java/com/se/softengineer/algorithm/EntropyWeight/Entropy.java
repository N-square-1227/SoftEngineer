package com.se.softengineer.algorithm.EntropyWeight;

import com.se.softengineer.dao.UsersMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Entropy {

    /**
     * 存放原始数据的数组
     */
    List<Double> dataList;
    public void algorithm() {
        for (int i = 0; i < dataList.size(); i++) {
            System.out.print(dataList.get(i) + " ");
        }

    }

    public static void main(String[] args) {
    }
}
