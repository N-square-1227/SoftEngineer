package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.se.softengineer.entity.Node;
import com.se.softengineer.mapper.NodeMapper;
import com.se.softengineer.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:44
 */
@Service
public class NodeServiceImpl implements NodeService {

    @Autowired
    private NodeMapper nodeMapper;

    public List<Node> queryNodeList() {
        QueryWrapper<Node> queryWrapper = new QueryWrapper<>();
//        System.out.println(nodeMapper.selectList(queryWrapper));
        return nodeMapper.selectList(queryWrapper);
    }

    @Override
    public boolean insertIntoSheet(String tableName, List<Node> nodeList) {
        // 鲁棒性
        if (StringUtils.isBlank(tableName) || nodeList == null || nodeList.isEmpty()) {
            return false;
        }

        // 插入数据到指定的表
        for (Node node : nodeList) {
            nodeMapper.insertIntoSheet(tableName, node.getNodeId(), node.getNodeName(),
                    node.getNodeType(), node.getNodeWeight(), node.getParentID());
        }
        return true;
    }


    @Override
    public void createTable(String table_name) {
        nodeMapper.createTable(table_name);
    }

    @Override
    public void dropExistTable(String table_name) {
        nodeMapper.dropExistTable(table_name);
    }

    @Override
    /* 读取indexsym数据表中的所有指标 */
    public List<Node> getIndex(String table_name) {
        return nodeMapper.getIndex(table_name);
    }
}
