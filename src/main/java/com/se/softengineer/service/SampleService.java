package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Sample;

<<<<<<< HEAD
import java.util.HashMap;
=======
>>>>>>> ae64deceea9317932cffef9f3ca9df382eda48db
import java.util.List;

public interface SampleService extends IService<Sample> {

    List<Sample> getData(String table_name);
    List<String> getColName(String table_name);
}