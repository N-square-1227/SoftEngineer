package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Node;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * by wxy
 * 试试springboot
 **/
@Mapper
public interface IndexSymMapper extends BaseMapper<Node> {
    List<Node> getIndex();
}