package com.se.softengineer.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.TreeData;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:43
 */
public interface IndexSymNodeService extends IService<IndexSymNode> {
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

    /**
     * @author lmy
     */
    Boolean saveJsonData(String tableName,String filePath) throws IOException;
    /**
     * @author xy
     */
    int createIndexSymTable(String tableName);

    /**
     * @author lmy
     */
    public List<TreeData> getIndexSymData(List<IndexSymNode> indexSym);

    int insertIntoTable(String tableName, String name, int type, double weight, int id);

}