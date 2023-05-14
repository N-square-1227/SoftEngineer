package com.se.softengineer.controller;

import com.se.softengineer.dao.IndexSymMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.utils.AnalyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.naming.ConfigurationException;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaxue
 * 导入指标体系文件
 */
@RestController
@RequestMapping("/import")
public class ImportController {

    //获取当前用户名
    public String userName;
    //从excel中读到的临时数据
    public static List<String[]> cells=new ArrayList<>();
    //用户导入的indexSym数据，除了在数据库中保存，在此全局变量中也保存了
    public static List<Indexsym> indexSym=new ArrayList<>();
    //每个用户都有一个tableName
    public static String indexSymTableName;

    @Autowired
    private IndexSymService indexSymService;

    @Autowired
    private IndexSymMapper indexSymMapper;
    @PostMapping(value = "/excel")
    //@RequestParam("file") MultipartFile file
    public void uploadFileByExcel(@RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        //保存到本地
        String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        List<String[]> l=AnalyExcel.readExcelFile(file,1);
        cells=l;
        //keepData(l);
    }

    public static String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 你的项目路径/FileSpace  也就是和src同级
        String path = "D:\\seData\\"+fileName;
        // 判断父文件夹是否存在
        File file = new File(path);
        System.out.println(file.getPath());
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        // 通过NIO保存文件到本地磁盘
        try {
            FileOutputStream fos = new FileOutputStream(path);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    @RequestMapping("/xml")
    public void loadFileByXML(){

    }

    @RequestMapping("/mysql")
    public void loadFileByMysql(){

    }

    @RequestMapping("/keepExcel")
    public void keep(){
        keepData(cells);
    }
    /**
     * 保存读取的indexSym,保存到List和数据库
     * @param list
     */
    public void keepData(List<String[]> list){
        Indexsym t=new Indexsym();
        //indexSymService.createTable("temp");
        userName="temp";
        indexSymTableName=userName+"indexSym";
        indexSymMapper.createTable(indexSymTableName);
        //System.out.println("xxxxxxxxxxxxxxx");
        for(String[] l:list){
            indexSymMapper.insertIntoTable(indexSymTableName,l[0],Integer.parseInt(l[1]),Double.parseDouble(l[2]),Integer.parseInt(l[3]));

        }
    }
}
