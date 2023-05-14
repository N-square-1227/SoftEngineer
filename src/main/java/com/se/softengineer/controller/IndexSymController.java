package com.se.softengineer.controller;

import com.se.softengineer.algorithm.pca.PCA;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.Node;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.SampleService;
import com.se.softengineer.service.IndexSymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * by wxy
 * 试试springboot
 **/

@RestController
@RequestMapping("/indexsym")
public class IndexSymController {

    @Autowired
    private IndexSymService indexSymService;

    @Autowired
    private SampleService sampleService;

    private final IndexSym indexSym = new IndexSym();
    private List<Sample> data;

    /**by wxy
     * Just for test
     **/
    @GetMapping("/test")
    private String test() {
        return "Hello World!";
    }


    /**
     * 启动项目后，地址栏里输入http://localhost:8877/indexsym/loadIndexSym
     * 页面和后台会输出加载进来的指标体系
     **/
    @GetMapping("/loadIndexSym")
    private List<Node> load_indexsym() {
        indexSym.setNodeList(indexSymService.getIndex());
        for(int i = 0; i < indexSym.getNodeList().size(); i ++)
            System.out.println(indexSym.getNodeList().get(i));
        return indexSym.getNodeList();
    }

    @GetMapping("/loadData")
    private List<Sample> load_data() {
        data = sampleService.getData();
        return data;
    }

    @GetMapping("/loadColumnNames")
    private List<String> load_columnNames() {
        return sampleService.getColName();
    }

    @GetMapping("/usePCA")
    public boolean use_PCA() {
        load_data();
        PCA pca = new PCA();
        return true;
    }

}
