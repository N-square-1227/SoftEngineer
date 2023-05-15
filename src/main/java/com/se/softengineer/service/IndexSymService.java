package com.se.softengineer.service;

<<<<<<< HEAD
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IndexSymService {
    void add(Indexsym indexsym);

=======
import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Indexsym;

import java.io.IOException;

public interface IndexSymService extends IService<Indexsym> {
    Boolean saveIndexSym(String tableName,String filePath) throws IOException;
    Boolean createTable(String tableName);
>>>>>>> ae64deceea9317932cffef9f3ca9df382eda48db
}
