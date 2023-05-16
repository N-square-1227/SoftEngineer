package com.se.softengineer.service;

import com.se.softengineer.entity.Indexsym;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;


public interface IndexSymService {
    void add(Indexsym indexsym);
    Boolean saveJson(String tableName,String filePath) throws IOException;
    Boolean createTable(String tableName);

}
