package com.se.softengineer.controller;

import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.service.OptimizeService;
import com.se.softengineer.service.SampleService;
import com.se.softengineer.utils.Result;
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
    private IndexSymService indexSymService;

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
    private Result test() {
        return Result.success("Hello World!");
    }


    /**
     * 获取指定数据表中存储的指标体系
     * http://localhost:8877/indexsym/loadIndexSym?table_name=indexsym
     **/
    @GetMapping("/loadIndexSym")
    private Result load_indexsym(String table_name) {
        indexSym.setNodeList(indexSymService.getIndex(table_name));
//        for(int i = 0; i < indexSym.getNodeList().size(); i ++)
//            System.out.println(indexSym.getNodeList().get(i));
        return indexSym.getNodeList().size() != 0 ? Result.success(indexSym.getNodeList()) : Result.fail();
    }

    /**
     * 获取指定数据表中的所有数据
     * http://localhost:8877/indexsym/usePCA?table_name=data
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/loadData")
    private Result load_data(String table_name) {
        data = sampleService.getData(table_name);
        return data.size() != 0 ? Result.success(data) : Result.fail() ;
    }

    /**
     * 根据指标体系生成对应的数据表（叶子节点是表头
     * @param table_name 指标体系表
     * @return 是否成功建表（啊？？怎么成功建了表return回来的是false啊
     * http://localhost:8877/indexsym/create_data_table?table_name=indexsym
     */
    @GetMapping("/create_data_table")
    private Result create_data_table(String table_name) {
        load_indexsym(table_name);
        indexSym.get_leaves();
        int leaf_num = indexSym.getLeaf_num();
        List<String> columnNames = new ArrayList<>();
        for(int i = 1; i <= leaf_num; i ++)
            columnNames.add("X" + i);
        return !sampleService.createDataTable(table_name+"_data", columnNames) ? Result.success() : Result.fail();
    }

    /**
     * 获取指定数据表的列名List
     * http://localhost:8877/indexsym/loadColumnNames?table_name=indexsym
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/loadColumnNames")
    private Result load_columnNames(String table_name) {
        columnList = sampleService.getColName(table_name);
        return columnList.size() != 0 ?Result.success(columnList):Result.fail();
    }

    /**
     * 调用PCA算法对指定的指标体系做优化
     * indexsym_name表示存储指定指标体系的数据表的名字
     * data_tablename表示对应的数据的数据表的名字
     * http://localhost:8877/indexsym/pca?indexsym_name=indexsym&&data_tablename=data
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/pca")
    public Result use_PCA(String indexsym_name, String data_tablename) {
        IndexSym newIndexSym = optimizeService.pca(indexsym_name, data_tablename);
        return newIndexSym!=null? Result.success(newIndexSym):Result.fail();
    }

    /**
     * @Author 南希诺
     * @create 2023.5.10
     * 熵权法
     * http://localhost:8877/indexsym/entropy?indexsym_name=indexsym&&data_tablename=data
     * @return
     */
    @GetMapping("/entropy")
    public Result use_entropy(String indexsym_name, String data_tablename) throws Exception {
        IndexSym newIndexSym = optimizeService.entropy(indexsym_name, data_tablename);
        return newIndexSym!=null? Result.success(newIndexSym):Result.fail();
    }

    /**
     * kmeans算法调用
     * @author xly
     * @return
     * http://localhost:8877/indexsym/kmeans?indexsym_name=indexsym&&data_tablename=data
     * @throws Exception
     */
    @GetMapping("/kmeans")
    public Result use_kmeans(String indexsym_name, String data_tablename) throws Exception {
        IndexSym newIndexSym = optimizeService.kmeans(indexsym_name, data_tablename);
        return newIndexSym!=null? Result.success(newIndexSym):Result.fail();
    }

}
