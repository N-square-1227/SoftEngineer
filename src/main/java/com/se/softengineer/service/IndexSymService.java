package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.TreeData;

import java.io.IOException;
import java.util.List;

public interface IndexSymService extends IService<Indexsym> {
    Boolean saveJsonData(String tableName,String filePath) throws IOException;
    Boolean createTable(String tableName);
    public List<Indexsym> getAllData(String tableName);
    public List<TreeData> getIndexSymData(List<Indexsym> indexSym);
}
