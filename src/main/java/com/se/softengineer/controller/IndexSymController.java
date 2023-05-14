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

import java.util.ArrayList;
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

    private IndexSym indexSym = new IndexSym();
    private List<Sample> data = new ArrayList<>();

    /**by wxy
     * Just for test
     **/
    @GetMapping("/test")
    private String test() {
        return "Hello World!";
    }


    /**
     * 获取指定数据表中存储的指标体系
     * http://localhost:8877/indexsym/loadIndexSym=indexsym
     **/
    @GetMapping("/loadIndexSym")
    private List<Node> load_indexsym(String table_name) {
        indexSym.setNodeList(indexSymService.getIndex(table_name));
//        for(int i = 0; i < indexSym.getNodeList().size(); i ++)
//            System.out.println(indexSym.getNodeList().get(i));
        return indexSym.getNodeList();
    }

    /**
     * 获取指定数据表中的所有数据
     * http://localhost:8877/indexsym/usePCA?table_name=data
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/loadData")
    private List<Sample> load_data(String table_name) {
        data = sampleService.getData(table_name);
        return data;
    }

    /**
     * 获取指定数据表的列名List
     * http://localhost:8877/indexsym/usePCA?table_name=index
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/loadColumnNames")
    private List<String> load_columnNames(String table_name) {
        return sampleService.getColName(table_name);
    }

    /**
     * 调用PCA算法对指定的指标体系做优化
     * indexsym_name表示存储指定指标体系的数据表的名字
     * data_tablename表示对应的数据的数据表的名字
     * http://localhost:8877/indexsym/usePCA?indexsym_name=indexsym&&data_tablename=data
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/usePCA")
    public IndexSym use_PCA(String indexsym_name, String data_tablename) {
        load_data(data_tablename);
        load_indexsym(indexsym_name);
        List<Node> leaves = indexSym.get_leaves();
        PCA pca = new PCA(data);
        pca.solve();
        for(int i = 0; i < pca.getFactor_num(); i ++) {
            List<Integer> son_nodes = pca.getNew_sym().getNodeTree().get(pca.getNew_sym().getNodeList().get(i).getNodeId());
            System.out.println(pca.getNew_sym().getNodeList().get(i).getNodeName());
            for (Integer son_node : son_nodes) {
                /* 第idx个叶子节点*/
                int idx = Integer.parseInt(pca.getNew_sym().getNodeList().get(son_node - 1).getNodeName());
                pca.getNew_sym().getNodeList().get(son_node - 1).setNodeName(leaves.get(idx - 1).getNodeName());
            }
        }
        return pca.getNew_sym();
    }

}
