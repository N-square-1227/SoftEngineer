package com.se.softengineer.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.mapper.IndexSymMapper;
import com.se.softengineer.service.IndexSymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:44
 */
@Service
public class IndexSymServiceImpl extends ServiceImpl<IndexSymMapper, IndexSymNode> implements IndexSymService {

    @Autowired
    private IndexSymMapper indexSymMapper;

    public List<IndexSymNode> queryNodeList() {
        QueryWrapper<IndexSymNode> queryWrapper = new QueryWrapper<>();
//        System.out.println(nodeMapper.selectList(queryWrapper));
        return indexSymMapper.selectList(queryWrapper);
    }

    @Override
    public boolean insertIntoSheet(String tableName, List<IndexSymNode> nodeList) {
        // 鲁棒性
        if (StringUtils.isBlank(tableName) || nodeList == null || nodeList.isEmpty()) {
            return false;
        }

        // 插入数据到指定的表
        for (IndexSymNode node : nodeList) {
            indexSymMapper.insertIntoTable_noauto(tableName, node.getNodeID(), node.getNodeName(),
                    node.getNodeType(), node.getNodeWeight(), node.getParentID());
        }
        return true;
    }

    public boolean insertIntoTable(String tableName, String nodeName, int nodeType, double nodeWeight, int parentID){
        return indexSymMapper.insertIntoTable(tableName, nodeName,
                nodeType, nodeWeight, parentID);
    }

    @Override
    public boolean insertIntoTable(String tableName, Integer nodeID, String nodeName, int nodeType, double nodeWeight, int parentID) {
        return indexSymMapper.insertIntoTable_noauto(tableName, nodeID, nodeName, nodeType, nodeWeight, parentID);
    }

    @Override
    public void createTable(String table_name) {
        indexSymMapper.createTable(table_name);
    }

    @Override
    public void dropExistTable(String table_name) {
        indexSymMapper.dropExistTable(table_name);
    }

    @Override
    /* 读取indexsym数据表中的所有指标 */
    public List<IndexSymNode> getIndex(String table_name) {
        return indexSymMapper.getIndex(table_name);
    }

    @Override
    public void add(IndexSymNode node) {
        indexSymMapper.insert(node);
    }
}
