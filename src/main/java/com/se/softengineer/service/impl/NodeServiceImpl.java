package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.se.softengineer.algorithm.indexsym.Node;
import com.se.softengineer.dao.NodeMapper;
import com.se.softengineer.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public boolean insertNodeList(String tableName, List<Node> nodeList) {
        if (StringUtils.isBlank(tableName) || nodeList == null || nodeList.isEmpty()) {
            return false;
        }

        // 插入数据到指定的表
        for (Node node : nodeList) {
            nodeMapper.insert(node);
        }
        return true;
    }
}
