package com.se.softengineer.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.dao.IndexSymMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.TreeData;
import com.se.softengineer.service.IndexSymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexSymServiceImpl extends ServiceImpl<IndexSymMapper, Indexsym> implements IndexSymService {
    @Autowired
    IndexSymMapper indexSymMapper;

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
            List<Indexsym> nodeList = JSON.parseArray(sb.toString(), Indexsym.class);
            for(Indexsym node : nodeList)
                indexSymMapper.insertIntoTable(tableName,node.getNodeName(),node.getNodeType(),node.getNodeWeight(),node.getParentID());   //插入数据
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @author lmy
     */
    @Override
    public Boolean createTable(String tableName) {
        return indexSymMapper.createTable(tableName)>0?true:false;
    }

    /**
     * @author lmy
     */
    @Override
    public List<Indexsym> getAllData(String tableName) {
        return indexSymMapper.selectAllData(tableName);
    }

    /**
     * @author lmy
     */
    @Override
    public List<TreeData> getIndexSymData(List<Indexsym> indexSym) {
        // 经过 buildIndexSym 处理过的 nodeList 中的 children 属性就都有数据了
        List<Indexsym> nodeList = buildIndexSym(indexSym);
        // 然后就是将 nodeList 处理成最终格式的数据
        return nodeList.stream().map(TreeData::new).collect(Collectors.toList());
    }

    /**
     * @author lmy
     */
    private List<Indexsym> buildIndexSym(List<Indexsym> nodes) {
        // 初步处理好的数据 即：添加childrenList
        List<Indexsym> returnList = new ArrayList<>();
        // 所有节点 id 的 List 集合
        List<Integer> tempList = new ArrayList<>();
        for (Indexsym node : nodes)
        {
            tempList.add(node.getNodeId());
        }
        // 遍历所有树节点（一个树节点就是数据库中的一条数据）
        for (Iterator<Indexsym> iterator = nodes.iterator(); iterator.hasNext();)
        {
            Indexsym node = iterator.next();
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
    private void recursionFn(List<Indexsym> nodeList, Indexsym curNode)
    {
        // 获取此节点的子节点列表
        List<Indexsym> childList = getChildList(nodeList, curNode);
        // 将子节点 List set 进去
        curNode.setChildren(childList);
        for (Indexsym tChild : childList)
        {
            //继续判断子节点的子节点
            if (hasChild(nodeList, tChild))
            {
                // 判断是否有子节点
                Iterator<Indexsym> it = childList.iterator();
                while (it.hasNext())
                {
                    Indexsym n =  it.next();
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
    private List<Indexsym> getChildList(List<Indexsym> list, Indexsym curNode)
    {
        List<Indexsym> childList = new ArrayList<>();
        Iterator<Indexsym> it = list.iterator();
        while (it.hasNext())
        {
            Indexsym n = it.next();
            //当n的父节点id为当前节点时 add
            if (n.getParentID() == curNode.getNodeId())
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
    private boolean hasChild(List<Indexsym> list, Indexsym t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

//    public static boolean isNotNull(Object obj) {
//        //noinspection ConstantConditions
//        return null != obj && false == obj.equals(null);
//    }

}
