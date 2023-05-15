package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.mapper.SampleMapper;
import com.se.softengineer.service.SampleService;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * by wxy
 * 试试springBoot*/
@Service
public class SampleServiceImpl extends ServiceImpl<SampleMapper, Sample> implements SampleService {

    @Resource
    SampleMapper sampleMapper;

    @Override
    public List<Sample> getData(String table_name) {
        List<HashMap<String, Double>> list = sampleMapper.getData(table_name);
        List<Sample> result = new ArrayList<>();
        for (HashMap<String, Double> map : list) {
//            System.out.println(map.values());
            Sample sample = new Sample();
            sample.setData(map.values().stream().toList());
            result.add(sample);
        }
        return result;
    }

    @Override
    public List<String> getColName(String table_name){
        return sampleMapper.getColName(table_name);
    }
}