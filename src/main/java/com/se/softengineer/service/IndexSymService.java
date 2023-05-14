package com.se.softengineer.service;

import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IndexSymService {
    void add(Indexsym indexsym);

}
