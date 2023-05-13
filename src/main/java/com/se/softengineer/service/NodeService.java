package com.se.softengineer.service;

import com.se.softengineer.algorithm.indexsym.Node;

import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:43
 */
public interface NodeService {

    /** 南希诺
     * 查询，select *
     * @return 数据库里的原生列表数据
     */
    List<Node> queryNodeList();

    /**
     * 南希诺
     * 将自己新的表存储在数据库中
     * @param nodeList 君の列表
     * @return 成功 T，or F
     */
    boolean insertIntoEntropy(String tableName, List<Node> nodeList);
}
