package com.se.softengineer.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.TreeData;
import com.se.softengineer.service.IndexSymNodeService;
import com.se.softengineer.service.OptimizeService;
import com.se.softengineer.service.SampleService;
import com.se.softengineer.utils.Result;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/indexSymNode")
public class IndexSymNodeController {
    @Autowired
    private IndexSymNodeService indexSymNodeService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private OptimizeService optimizeService;

    /**
     * @author lmy
     * http://localhost:8877/indexSymNode/getTreeData?tableName=indexsym&func=pca
     */
    @GetMapping("/getTreeData")
    public Result getTreeData(@RequestParam("tableName") String tableName,@RequestParam("func") String func) throws Exception {
        /** by wxy
         * 由于user_data表改为在用户注册时创建，所以有可能用户没有上传过指标体系，此时table_name为空
         * 应该在页面返回提示信息
         **/
        if(tableName == null || tableName.equals(""))
            return Result.fail();
        if(func == null || func.equals(""))
            return Result.fail();

        HashMap<String,Object> res_map = new HashMap<>();

        /**
         * 判断func 调用算法 接收 List<IndexSymNode>数据
         * by wxy
         */
        IndexSym indexSym;
        if(func.equals("kmeans")) {
            List<Double> sllList = new ArrayList<>();
            indexSym = optimizeService.kmeans(tableName, tableName + "_data",sllList);
            res_map.put("SSE",sllList);
            if(sllList.size()==0)
                return Result.fail();

        } else if (func.equals("entropy")) {        //  熵权法
            Pair<IndexSym, Map<String, List<Object>>> pair = optimizeService.entropy(tableName, tableName + "_data");
            indexSym = pair.getKey();
            Map<String, List<Object>> map = pair.getValue();

            List<Map<String, Double>> result = new ArrayList<>(50);
            List<Object> names = map.get("names");
            List<Object> weight = map.get("weight");
            for (int i = 0; i < names.size(); i++) {
                Map<String, Double> temp = new HashMap<>();
                temp.put((String) names.get(i), (Double) weight.get(i));
                result.add(temp);
            }

            res_map.put("en", result);
//            System.out.println(result);
//            res_map.put("names", names);
//            res_map.put("weight", weight);
//
//
//            System.out.println(names);
//            System.out.println();
//            System.out.println(weight);

        }else if(func.equals("pca")){
            Map<String, Object> pca_res = optimizeService.pca(tableName, tableName + "_data");
            indexSym = (IndexSym) pca_res.get("indexsym");
            /* 返回载荷矩阵和阈值到前端 */
            /* 救命感觉前端越来越慢了 */
            res_map.put("loadmatrix", pca_res.get("loadmatrix"));
            res_map.put("threshold", pca_res.get("threshold"));
            res_map.put("indicators", pca_res.get("indicators"));
        }else {
            return Result.fail();
        }
        //System.out.println(tableName+func);
        // 先查出来表中的所有数据
        /** by wxy
         * 算法返回的结果是Indexsym类，NodeList可以直接从实例对象中获取
         * 算法生成的新指标体系存储的数据表名为： 原本的表名_new_算法名
         * 原本的表名_new_entropy  原本的表名_new_kmeans  原本的表名_new_pca
         **/
        List<IndexSymNode> indexSymNodes = indexSym.getNodeList();
        // 转换成画树需要的类
        List<TreeData> treeData=indexSymNodeService.getIndexSymData(indexSymNodes);
        JSONArray jsonArray=getJsonList(treeData);

        if(jsonArray.size() == 0)
            return Result.fail();


        res_map.put("treeData",jsonArray);
        /* 改到重新请求一次吧*/
//        jsonArray.add(optimizeService.caculateResult(tableName + "_data", tableName, newindexname));
//        jsonArray.add(optimizeService.caculateResult(tableName + "_data", tableName, tableName));
        // 返回构建好的数据
        return Result.success(res_map);
    }

    @GetMapping("/getOriginalTreeData")
    public Result getOriginalTreeData(@RequestParam("tableName") String tableName) throws Exception {

        List<IndexSymNode> indexSym = indexSymNodeService.getIndex(tableName);
        // 转换成画树需要的类
        List<TreeData> treeData=indexSymNodeService.getIndexSymData(indexSym);
        JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(treeData));
        // 返回构建好的数据
        return jsonArray.size()>0?Result.success(jsonArray):Result.fail();
    }

    /**
     * @author lmy
     */
    public JSONArray getJsonList(List<TreeData> list){
        System.out.println(JSONArray.parseArray(JSON.toJSONString(list)));
        return JSONArray.parseArray(JSON.toJSONString(list));
    }
}
