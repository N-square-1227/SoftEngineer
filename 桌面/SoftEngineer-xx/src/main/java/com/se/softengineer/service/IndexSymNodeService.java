package com.se.softengineer.service;


import com.se.softengineer.entity.IndexSymNode;

import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:43
 */
public interface IndexSymNodeService {
    /**
     * 查询，select *
     * @return 数据库里的原生列表数据
     */
    List<IndexSymNode> queryNodeList();

    /**
     * 将自己新的表存储在数据库中
     * @param nodeList 君の列表
     * @return 成功 T，or F
     */
    boolean insertIntoSheet(String tableName, List<IndexSymNode> nodeList);

    List<IndexSymNode> getIndex(String table_name);

    /* by nxn */
    void createTable(String table_name);
    /* by nxn */
    void dropExistTable(String table_name);
}