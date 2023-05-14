package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.se.softengineer.dao.IndexSymMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.IndexSymService;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexSymImpl implements IndexSymService {
    @Autowired
    private IndexSymMapper indexSymMapper;


    @Override
    public void add(Indexsym indexsym) {
        indexSymMapper.insert(indexsym);
    }



}
