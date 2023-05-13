package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Sample;

import java.util.List;

public interface SampleService extends IService<Sample> {

    List<Sample> getData();
}
