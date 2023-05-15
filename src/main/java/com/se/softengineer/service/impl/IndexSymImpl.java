package com.se.softengineer.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.se.softengineer.dao.IndexSymMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.IndexSymService;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class IndexSymImpl implements IndexSymService {
    @Autowired
    private IndexSymMapper indexSymMapper;


    @Override
    public void add(Indexsym indexsym) {
        indexSymMapper.insert(indexsym);
    }
    @Override
    public Boolean saveJson(String tableName,String filePath) throws IOException {
        try{
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
            List<Indexsym> nodeList = JSON.parseArray(sb.toString(), Indexsym.class);
            for(Indexsym node : nodeList)
                indexSymMapper.insertIntoTable(tableName,node.getNodeName(),node.getNodeType(),node.getNodeWeight(),node.getParentID());   //插入数据
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean createTable(String tableName) {
        return indexSymMapper.createTable(tableName)>0?true:false;
    }


}
