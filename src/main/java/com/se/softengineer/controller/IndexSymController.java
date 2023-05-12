package com.se.softengineer.controller;

import com.se.softengineer.algorithm.indexsym.Data;
import com.se.softengineer.algorithm.pca.PCA;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.Node;
import com.se.softengineer.service.IndexSymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final IndexSym indexSym = new IndexSym();
    /*private List<Data> data;
    // 想试一下这种写法能不能载入数据*/

    /**by wxy
     * Just for test
     **/
    @GetMapping("/test")
    private String test() {
        return "Hello World!";
    }

    @GetMapping("/loadData")
    private List<Node> load_data() {
        indexSym.setNodeList(indexSymService.getIndex());
        for(int i = 0; i < indexSym.getNodeList().size(); i ++)
            System.out.println(indexSym.getNodeList().get(i));
        return indexSym.getNodeList();
    }

    @GetMapping("/usePCA")
    public boolean use_PCA() {
        PCA pca = new PCA();
        return true;
    }

}
