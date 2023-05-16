/*
package com.se.softengineer.controller;

import com.se.softengineer.entity.IndexSymWxy;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.IndexSymNodeService;
import com.se.softengineer.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * by wxy
 * 试试springboot
 **//*


@RestController
@RequestMapping("/indexsym")
public class IndexSymController {

    @Autowired
    private IndexSymNodeService nodeService;

    @Autowired
    private SampleService sampleService;


    private IndexSymWxy indexSymWxy = new IndexSymWxy();
    private List<Sample> data = new ArrayList<>();

    private List<String> columnList = new ArrayList<>();

    */
/**by wxy
     * Just for test
     **//*

    @GetMapping("/test")
    private String test() {
        return "Hello World!";
    }


    */
/**
     * 获取指定数据表中存储的指标体系
     * http://localhost:8877/indexsym/loadIndexSym=indexsym
     **//*

    @GetMapping("/loadIndexSym")
    private List<IndexSymNode> load_indexsym(String table_name) {
        indexSymWxy.setNodeList(nodeService.getIndex(table_name));
        return indexSymWxy.getNodeList();
    }


    */
/**
     * 根据指标体系生成对应的数据表（叶子节点是表头
     * @param table_name 指标体系表
     * @return 是否成功建表（啊？？怎么成功建了表return回来的是false啊
     * http://localhost:8877/indexsym/create_data_table?table_name=indexsym
     *//*

    @GetMapping("/create_data_table")
    public boolean create_data_table(String table_name) {
        load_indexsym(table_name);
        indexSymWxy.get_leaves();
        int leaf_num = indexSymWxy.getLeaf_num();
        List<String> columnNames = new ArrayList<>();
        for(int i = 1; i <= leaf_num; i ++)
            columnNames.add("X" + i);
        return sampleService.createDataTable(table_name+"_data", columnNames);
    }





}*/
