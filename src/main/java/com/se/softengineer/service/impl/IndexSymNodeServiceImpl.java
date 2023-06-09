package com.se.softengineer.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.TreeData;
import com.se.softengineer.mapper.IndexSymMapper;
import com.se.softengineer.mapper.IndexSymNodeMapper;
import com.se.softengineer.service.IndexSymNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:44
 */
@Service
public class IndexSymNodeServiceImpl  extends ServiceImpl<IndexSymNodeMapper, IndexSymNode> implements IndexSymNodeService {

    @Autowired
    private IndexSymNodeMapper nodeMapper;

    @Override
    public void createTable(String table_name) {
        nodeMapper.createTable(table_name);
    }

    @Override
    public void dropExistTable(String table_name) {
        nodeMapper.dropExistTable(table_name);
    }

    @Override
    /* 读取indexsym数据表中的所有指标 */
    public List<IndexSymNode> getIndex(String table_name) {
        List<IndexSymNode> nodeList = new ArrayList<>();
        try {
            nodeList = nodeMapper.getIndex(table_name);
            return nodeList;
        }catch (Exception e) {
            return nodeList;
        }
    }

    public Boolean isContainNull(List<IndexSymNode> nodeList){
        System.out.println("isContainNull");
        for(IndexSymNode node : nodeList) {
            System.out.println(node.toString());
            if (node.getNodeID() == null || node.getNodeName() == null || node.getNodeType() == null || node.getNodeWeight() == null || node.getParentID() == null) {
                System.out.println("true");
                return true;
            }
            if(node.getChildren().size()!=0)
                if(isContainNull(node.getChildren()))
                    return true;
        }
        return false;
    }

    /**
     * @author lmy
     */
    @Override
    public Boolean saveJsonData(String tableName,String filePath) throws IOException {
        try{
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
            List<IndexSymNode> nodeList = JSON.parseArray(sb.toString(), IndexSymNode.class);
            //判断数据是否正确
            if(isContainNull(nodeList))
                return false;
//            for(IndexSymNode node : nodeList)
//                nodeMapper.insertIntoTable(tableName,node.getNodeName(),node.getNodeType(),node.getNodeWeight(),node.getParentID());   //插入数据
            if(!insertJson(tableName,nodeList,false,0)){
                nodeMapper.dropExistTable(tableName);
                return false;
            }
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertJson(String tableName,List<IndexSymNode> nodeList,boolean isChild,int parentID){
        for(IndexSymNode node : nodeList) {
            System.out.println(parentID);
            System.out.println(node.toString());
            if(isChild && node.getParentID()==parentID)
                nodeMapper.insertIntoTable2(tableName,node.getNodeID(),node.getNodeName(),node.getNodeType(),node.getNodeWeight(),node.getParentID());   //插入数据
            else if(!isChild)
                nodeMapper.insertIntoTable2(tableName,node.getNodeID(),node.getNodeName(),node.getNodeType(),node.getNodeWeight(),node.getParentID());   //插入数据
            else
                return false;
            //如果有子节点
            if(node.getChildren().size()!=0)
                if(!insertJson(tableName,node.getChildren(),true,node.getNodeID()))
                    return false;
        }
        return true;
    }

    /**
     * @author lmy
     */
    @Override
    public int createIndexSymTable(String tableName) {
        return nodeMapper.createIndexSymTable(tableName);
    }

    /**
     * @author lmy
     */
    @Override
    public List<TreeData> getIndexSymData(List<IndexSymNode> indexSym) {
        // 经过 buildIndexSym 处理过的 nodeList 中的 children 属性就都有数据了
        List<IndexSymNode> nodeList = buildIndexSym(indexSym);
        // 然后就是将 nodeList 处理成最终格式的数据
        return nodeList.stream().map(TreeData::new).collect(Collectors.toList());
    }

    @Override
    public int insertIntoTable(String tableName, String name, int type, double weight, int id) {

        return nodeMapper.insertIntoTable(tableName,name,type,weight,id);
    }

    /**
     * @author lmy
     */
    private List<IndexSymNode> buildIndexSym(List<IndexSymNode> nodes) {
        // 初步处理好的数据 即：添加childrenList
        List<IndexSymNode> returnList = new ArrayList<>();
        // 所有节点 id 的 List 集合
        List<Integer> tempList = new ArrayList<>();
        for (IndexSymNode node : nodes)
        {
            tempList.add(node.getNodeID());
        }
        // 遍历所有树节点（一个树节点就是数据库中的一条数据）
        for (Iterator<IndexSymNode> iterator = nodes.iterator(); iterator.hasNext();)
        {
            IndexSymNode node = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(node.getParentID()))
            {
                // 递归处理数据，此处传入的 natureTree 是最顶级节点，没有父节点
                recursionFn(nodes, node);
                returnList.add(node);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = nodes;
        }
        System.out.println(returnList.size());
        for(int i=0;i<returnList.size();i++)
            System.out.println(returnList.get(i).toString());
        return returnList;
    }

    /**
     * 递归列表
     * @author lmy
     */
    private void recursionFn(List<IndexSymNode> nodeList, IndexSymNode curNode)
    {
        // 获取此节点的子节点列表
        List<IndexSymNode> childList = getChildList(nodeList, curNode);
        // 将子节点 List set 进去
        curNode.setChildren(childList);
        for (IndexSymNode tChild : childList)
        {
            //继续判断子节点的子节点
            if (hasChild(nodeList, tChild))
            {
                // 判断是否有子节点
                Iterator<IndexSymNode> it = childList.iterator();
                while (it.hasNext())
                {
                    IndexSymNode n =  it.next();
                    // 递归
                    recursionFn(nodeList, n);
                }
            }
        }
    }

    /**
     * 获取子节点列表
     * @author lmy
     */
    private List<IndexSymNode> getChildList(List<IndexSymNode> list, IndexSymNode curNode)
    {
        List<IndexSymNode> childList = new ArrayList<>();
        Iterator<IndexSymNode> it = list.iterator();
        while (it.hasNext())
        {
            IndexSymNode n = it.next();
            //当n的父节点id为当前节点时 add
            if (n.getParentID() == curNode.getNodeID())
            {
                childList.add(n);
            }
        }
        return childList;
    }

    /**
     * 判断是否有子节点
     * @author lmy
     */
    private boolean hasChild(List<IndexSymNode> list, IndexSymNode t)
    {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public List<IndexSymNode> getAllNodeInfo(String tableName) {
        return nodeMapper.getAllNodeInfo(tableName);
    }
}