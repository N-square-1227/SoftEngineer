package com.se.softengineer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.se.softengineer.algorithm.caculate.CaculateResult;
import com.se.softengineer.algorithm.caculate.CaculateSample;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.IndexSymResult;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.service.OptimizeService;
import com.se.softengineer.service.SampleService;
import com.se.softengineer.utils.QueryPageParam;
import com.se.softengineer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @PostMapping("/nodeListPage")
    public Result nodeListPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String table_name = (String)param.get("table_name");
        String queryContent = (String)param.get("query_nodeName");
//        System.out.println(table_name);

        Page<IndexSymNode> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<IndexSymNode> lambdaQueryWrapper = new LambdaQueryWrapper();
        /* 条件 */
        if (StringUtils.isNotBlank(queryContent) && !("null".equals(queryContent)))
            lambdaQueryWrapper.like(IndexSymNode::getNodeName, queryContent);

        IPage result = indexSymService.nodePaged(page,table_name,lambdaQueryWrapper);
        System.out.println("total=="+result.getTotal());
        return Result.success(result.getRecords(), result.getTotal());
    }

    /**
     * 获取指定数据表中的所有数据
     * http://localhost:8877/indexsym/loadData?table_name=data
     * 后面也可以改成PostMapping
     **/
    @GetMapping("/loadData")
    private Result load_data(String table_name) {
        data = sampleService.getData(table_name);
        return data.size() != 0 ? Result.success(data) : Result.fail() ;
    }

    /**
     * 分页显示指标体系数据
     * 后面还是迁到service里，controller里写这么多太累赘了
     **/
    @PostMapping("loadNewData")
    private Result load_new_data(@RequestBody QueryPageParam params) {
        try {
            HashMap param = params.getParam();
            String table_name = (String) param.get("basicTableName");
            String func = (String) param.get("func");

            String new_tablename = table_name + "_new" + "_" + func;
            String data_tablename = table_name + "_data";

            IndexSym origin_sym = new IndexSym(indexSymService.getIndex(table_name));
            IndexSym new_sym = new IndexSym(indexSymService.getIndex(new_tablename));
            List<Sample> samples = sampleService.getData(data_tablename);

            CaculateResult caculateResult = new CaculateResult(samples.get(0).getData(), origin_sym, new_sym);
            /* 新指标体系中的叶子节点的id对应原本的指标中的叶子节点索引 */
            Map<Integer, Integer> node_map = caculateResult.getNodeMap();

//            int nodeNums = origin_sym.getNodeList().size();
            int nodeNums = new_sym.getNodeList().size();
            List<Integer> new_dataCols = new ArrayList<>();
            for (int i = 0; i < nodeNums; i++) {
                int node_id = new_sym.getNodeList().get(i).getNodeID();
                if (node_map.containsKey(node_id)) {
                    new_dataCols.add(node_map.get(node_id));
                }
            }

            List<Sample> new_data = new ArrayList<>();
            for (Sample value : samples) {
                Sample sample = new Sample();
                for (Integer new_dataCol : new_dataCols)
                    sample.getData().add(value.getData().get(new_dataCol));
                new_data.add(sample);
            }
            HashMap<String, Object> res_map = new HashMap<>();
            res_map.put("sampleData", new_data);
            res_map.put("colNum", new_dataCols.size());
            res_map.put("sampleNum", new_data.size());
            return Result.success(res_map);
        }catch (Exception e) {
            return Result.fail();
        }
        /* 分页去前端实现了 */
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
     * http://localhost:8877/indexsym/loadColumnNames?table_name=data
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

    /*
    * http://localhost:8877/indexsym/caculateResult?dataName=wxy_indexsym_data&indexName=wxy_indexsym&newindexName=wxy_indexsym_new_entropy
    * 这太长了，改post
    **/
    @PostMapping("/caculateResult")
//    public Result use_caculateResult(String dataName, String indexName, String newindexName){
    public Result use_caculateResult(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String dataName = (String) param.get("dataName");
        String indexName = (String) param.get("indexName");
        String newindexName = (String) param.get("newindexName");

        List<IndexSymResult> res = optimizeService.caculateResult(dataName, indexName, newindexName);
        return res != null ? Result.success(res) : Result.fail();
    }


    /** by wxy
     * 调用方法类CaculateSample
     * 获取某一组样例各个节点的计算结果
     * 返回值为CaculateSample的内部类，包含一个Map和一个List
     * res: List是各个节点的值，按照节点id排列
     * res_map: map是节点id到节点值的映射，按需取用
     **/
    @PostMapping("/caculateSample")
    public Result caculateSample(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();

        String table_name = (String) param.get("table_name");
//        System.out.println(table_name);
        LinkedHashMap data = ((LinkedHashMap) param.get("sample"));
        List<Double> list = new ArrayList<>();
        for(Object key : data.keySet())
            try {
                list.add((double) data.get(key));
            }catch (Exception e) {    // 有整数类型，会报类型转换错误
                list.add((double)(int) data.get(key));
            }
        list.remove(0);
        Sample sample = new Sample(list);
//        System.out.println(sample.getData());
        IndexSym indexSym = new IndexSym(indexSymService.getIndex(table_name));
        return Result.success((new CaculateSample(indexSym, sample).caculate()));
    }

}
