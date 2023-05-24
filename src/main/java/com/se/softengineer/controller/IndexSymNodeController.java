package com.se.softengineer.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.se.softengineer.algorithm.caculate.CaculateResult;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.entity.TreeData;
import com.se.softengineer.service.IndexSymNodeService;
import com.se.softengineer.service.OptimizeService;
import com.se.softengineer.service.SampleService;
import com.se.softengineer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        /**
         * 判断func 调用算法 接收 List<IndexSymNode>数据
         * by wxy
         */
        IndexSym indexSym;
        String newindexname = "";
        if(func.equals("kmeans")) {
            indexSym = optimizeService.kmeans(tableName, tableName + "_data");
            newindexname = tableName + "_new" + "_kmeans";
        } else if (func.equals("entropy")) {
            indexSym = optimizeService.entropy(tableName, tableName + "_data");
            newindexname = tableName + "_new" + "_entropy";
        }else if(func.equals("pca")){
            indexSym = optimizeService.pca(tableName, tableName + "_data");
            newindexname = tableName + "_new" + "_pca";
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
        jsonArray.add(optimizeService.caculateResult(tableName + "_data", tableName, newindexname));
        jsonArray.add(optimizeService.caculateResult(tableName + "_data", tableName, tableName));
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
