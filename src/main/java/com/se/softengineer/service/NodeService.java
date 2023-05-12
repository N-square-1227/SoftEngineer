package com.se.softengineer.service;

import com.se.softengineer.algorithm.indexsym.Node;

import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:43
 */
public interface NodeService {
    /**
     * 查询，select *
     * @return
     */
    List<Node> queryNodeList();

    /**
     * 将自己新的表存储在数据库中
     * @param nodeList 你自己的 indexsym 表
     * @return 成功 T，or F
     */
    boolean insertNodeList(String tableName, List<Node> nodeList);
}
