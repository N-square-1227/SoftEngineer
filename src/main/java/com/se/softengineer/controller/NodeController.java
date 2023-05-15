//package com.se.softengineer.controller;
//
//import com.se.softengineer.service.IndexSymService;
//import com.se.softengineer.utils.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/node")
//public class NodeController {
//    @Autowired
//    private IndexSymService nodeService;
//    /**
//     * 文件保存路径
//     */
//    @Value("${file-save-path}")
//    private String fileSavePath;
//
//    @GetMapping("/saveNodeList")
//    public Result saveNodeList(){
//        String filePath=fileSavePath+"example.json";
//        boolean result=true;
//        try {
//            result = nodeService.saveNodeList(filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result==true?Result.success():Result.fail();
//    }
//}
