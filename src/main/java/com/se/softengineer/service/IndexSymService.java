package com.se.softengineer.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.se.softengineer.entity.IndexSymNode;

import java.io.IOException;
import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:43
 */
public interface IndexSymService {

    /**
     * 将自己新的表存储在数据库中
     * @param nodeList 君の列表
     * @return 成功 T，or F
     */
    boolean insertIntoSheet(String tableName, List<IndexSymNode> nodeList);

    IPage nodePaged(IPage<IndexSymNode> page, String table_name, Wrapper wrapper);

    List<IndexSymNode> getIndex(String table_name);

    /* by nxn */
    void createTable(String table_name);
    /* by nxn */
    void dropExistTable(String table_name);

    void add(IndexSymNode node);

}