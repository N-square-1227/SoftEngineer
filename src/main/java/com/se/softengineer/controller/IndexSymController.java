package com.se.softengineer.controller;

import com.se.softengineer.algorithm.EntropyWeight.Entropy;
import com.se.softengineer.algorithm.dataprocess.DataNumpy;
import com.se.softengineer.algorithm.pca.PCA;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.Node;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.NodeService;
import com.se.softengineer.service.OptimizeService;
import com.se.softengineer.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * by wxy
 * 试试springboot
 **/

@RestController
@RequestMapping("/indexsym")
public class IndexSymController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private OptimizeService optimizeService;

    private IndexSym indexSym = new IndexSym();
    private List<Sample> data = new ArrayList<>();

    private List<String> columnList = new ArrayList<>();

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
        indexSym.setNodeList(nodeService.getIndex(table_name));
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
        columnList = sampleService.getColName(table_name);
        return columnList;
    }

    /**
     * 调用PCA算法对指定的指标体系做优化
     * indexsym_name表示存储指定指标体系的数据表的名字
     * data_tablename表示对应的数据的数据表的名字
     * http://localhost:8877/indexsym/usePCA?indexsym_name=indexsym&&data_tablename=data
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/pca")
    public IndexSym use_PCA(String indexsym_name, String data_tablename) {
        return optimizeService.pca(indexsym_name, data_tablename);
    }

    /**
     * @Author 南希诺
     * @create 2023.5.10
     * 熵权法
     * http://localhost:8877/indexsym/entropy?indexsym_name=indexsym&&data_tablename=data
     * @return
     */
    @GetMapping("/entropy")
    public boolean use_entropy(String indexsym_name, String data_tablename) throws Exception {
        return optimizeService.entropy(indexsym_name, data_tablename);
    }

    /**
     * kmeans算法调用
     * @author xly
     * @return
     * http://localhost:8877/indexsym/kmeans?indexsym_name=indexsym&&data_tablename=data
     * @throws Exception
     */
    @GetMapping("/kmeans")
    public boolean use_kmeans(String indexsym_name, String data_tablename) throws Exception {
        return optimizeService.kmeans(indexsym_name, data_tablename);
    }

}
