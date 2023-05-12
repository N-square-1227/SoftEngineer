package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Node;
import com.se.softengineer.mapper.IndexSymMapper;
import com.se.softengineer.service.IndexSymService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * by wxy
 * 试试springBoot*/
@Service
public class IndexSymServiceImpl extends ServiceImpl<IndexSymMapper, Node> implements IndexSymService {

    @Resource
    private IndexSymMapper indexSymMapper;


    @Override
    /* 读取indexsym数据表中的所有指标 */
    public List<Node> getIndex() {
        return indexSymMapper.getIndex();
    }
}
