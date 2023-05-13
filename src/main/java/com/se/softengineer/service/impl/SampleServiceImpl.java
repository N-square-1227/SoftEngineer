package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.mapper.SampleMapper;
import com.se.softengineer.service.SampleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * by wxy
 * 试试springBoot*/
@Service
public class SampleServiceImpl extends ServiceImpl<SampleMapper, Sample> implements SampleService {

    @Resource
    SampleMapper SampleMapper;

    @Override
    public List<Sample> getData() {
        return SampleMapper.getData();
    }
}
