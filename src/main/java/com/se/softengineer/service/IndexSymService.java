package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.TreeData;

import java.io.IOException;
import java.util.List;

public interface IndexSymService extends IService<IndexSymNode> {
    Boolean saveJsonData(String tableName,String filePath) throws IOException;
    Boolean createIndexSymTable(String tableName);
    public List<IndexSymNode> getAllData(String tableName);
    public List<TreeData> getIndexSymData(List<IndexSymNode> indexSym);
}
