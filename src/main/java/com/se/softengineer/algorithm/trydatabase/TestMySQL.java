package com.se.softengineer.algorithm.trydatabase;

import com.se.softengineer.algorithm.indexsym.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * 没做数据库部分，简单连一下
 * 到时候应该是自动装配啥的，我不太懂
 **/
public class TestMySQL {

    public Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/se?useUnicode=true&characterEncoding=utf8&useSSL=false";
        return DriverManager.getConnection(url, "root", "");
    }

    /**
     * 获取指标体系各节点的相关信息
     **/
    public List<Node> querySym(String table) throws Exception {
        List<Node> nodeList = new ArrayList<>();
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        String sql = "select * from " + table;
        ResultSet rs = stmt.executeQuery(sql);
        /* 这部分应该可以直接装配，简单写了一下 */
        while (rs.next()) {
            Node node = new Node();
//            node.setNode_id(rs.getInt("nodeID"));
//            node.setNode_name(rs.getString("nodeName"));
//            node.setNode_type(rs.getInt("nodeType"));
//            node.setNode_weight(rs.getDouble("nodeWeight"));
//            node.setFrnode_id(rs.getInt("ParentID"));
            nodeList.add(node);
        }
        rs.close();
        stmt.close();
        con.close();
        return nodeList;
    }

    /**
     * 获取存放数据的各节点的相关信息
     **/
    public List<List<Double>> queryData(String table) throws Exception{
        List<List<Double>> dataList = new ArrayList<>();
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        String sql = "select * from " + table;
        ResultSet rs = stmt.executeQuery(sql);
        /* 这部分应该可以直接装配，简单写了一下 */
        int col_num = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            List<Double> sample = new ArrayList<>();
            for (int i = 1; i <= col_num; i++)
                sample.add(rs.getDouble(i));
            dataList.add(sample);
        }
        rs.close();
        stmt.close();
        con.close();
        return dataList;
    }
}
