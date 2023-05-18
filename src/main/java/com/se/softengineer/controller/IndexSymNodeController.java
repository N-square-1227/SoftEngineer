package com.se.softengineer.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.TreeData;
import com.se.softengineer.service.IndexSymNodeService;
import com.se.softengineer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/indexSymNode")
public class IndexSymNodeController {
    @Autowired
    private IndexSymNodeService indexSymNodeService;

    /**
     * @author lmy
     */
    @GetMapping("/getTreeData")
    public Result getTreeData(@RequestParam String tableName)
    {
        // 先查出来表中的所有数据
        List<IndexSymNode> indexSym = indexSymNodeService.getIndex(tableName);
        // 转换成画树需要的类
        List<TreeData> treeData=indexSymNodeService.getIndexSymData(indexSym);
        JSONArray jsonArray=getJsonList(treeData);
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
