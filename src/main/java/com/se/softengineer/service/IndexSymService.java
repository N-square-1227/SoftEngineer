package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Node;

import java.util.List;

/**
 * by wxy
 * 试试springBoot
 **/
public interface IndexSymService extends IService<Node> {
    List<Node> getIndex();
}
