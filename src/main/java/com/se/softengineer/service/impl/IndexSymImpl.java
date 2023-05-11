package com.se.softengineer.service.impl;

import com.se.softengineer.dao.IndexSymMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.IndexSymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexSymImpl implements IndexSymService {
    @Autowired
    private IndexSymMapper indexSymMapper;

    @Override
    public List<Indexsym> selectAll(){
        List<Indexsym> t=indexSymMapper.selectList(null);
        return t;
    }
}
