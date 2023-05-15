package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Indexsym;

import java.io.IOException;

public interface IndexSymService extends IService<Indexsym> {
    Boolean saveIndexSym(String tableName,String filePath) throws IOException;
    Boolean createTable(String tableName);
}
