package com.se.softengineer.algorithm.Kmeans;

import com.se.softengineer.algorithm.dataprocess.DataNumpy;
import com.se.softengineer.entity.Sample;

import java.util.*;

/**
 * @author xly
 */
public class Kmeans {
    /**
     * data中每一列的值为每个点的坐标
     */
    private List<Sample> datas;
    private List<Point> pointList;//存放原始数据集构成的点集
    private DistanceCompute disC = new DistanceCompute();

    private int kNum;       //簇的数目
    private int iterRunTimes = 0;//单词迭代实际运行次数

    private int len = 0;    //每个数据点的维度
    private Double disdiff = (Double) 0.01;//单次迭代终止体哦阿健，两次运行中类中心的距离差
    //处理数据的类

    public Kmeans(int k,List<Sample> datas){
        //首先对数据矩阵专职，每一行数据中的值对应每一个点的坐标
        this.datas = datas;
        DataNumpy.transposition(this.datas);
        List<List<Double>> data = getDataList();
        this.kNum = k;
        this.len = data.get(0).size();
        Init(data);
    }

    public List<List<Double>> getDataList() {
        List<List<Double>> result = new ArrayList<>();
        for(Sample sample : datas) {
            result.add(sample.getData());
        }
        return result;
    }

    /**
     * 初始化数据集
     */
    private void Init(List<List<Double>> data){
        List<Point> numpointList = new ArrayList<>();
        for(int i=0;i<data.size();i++){
            double[] list = new double[data.get(i).size()];
            for(int j=0;j<data.get(i).size();j++)
                list[j] =  data.get(i).get(j);
            numpointList.add(new Point(i,list));
        }
        pointList = normalize(numpointList);
    }

    /**
     *对数据进行归一化处理
     */
    public List<Point> normalize(List<Point> arrayList){
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < arrayList.size(); i++) {
            double[] point = arrayList.get(i).getlocalArray();
            for (int j = 0; j < point.length; j++) {
                if (point[j] < min) {
                    min = point[j];
                }
                if (point[j] > max) {
                    max = point[j];
                }
            }
        }
        List<Point> normalizedArray = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            double[] point = arrayList.get(i).getlocalArray();
            double[] newPoint = new double[arrayList.get(i).getlocalArray().length];
            for (int j = 0; j < point.length; j++) {
                newPoint[j] = (point[j]-min)/(max-min);
            }
            normalizedArray.add(new Point(i,newPoint));
        }
        return normalizedArray;
    }

    /**
     * 随机选取中心点，构成中心类
     */
    public Set<Cluster> chooseCenterCluster(){
        Set<Cluster> clusterSet = new HashSet<>();
        Random random = new Random();
        for(int id = 0;id<kNum;){
            Point point = pointList.get(random.nextInt(pointList.size()));
            //标记是否已经选择过该数据
            boolean flag = true;
            for(Cluster cluster : clusterSet){
                if(cluster.getCenter().equals(point))
                    flag = false;
            }
            //如果随机选取的点没有被选中，则生成一个cluster
            if(flag){
                Cluster cluster = new Cluster(id,point);
                clusterSet.add(cluster);
                id++;
            }
        }
        return clusterSet;
    }

    /**
     * 为每个点分配一个类
     */
    public void cluster(Set<Cluster> clusterSet){
        //计算每个点到K个中心的距离，并且为每个点标记类记号
        for(Point point:pointList){
            double min_dis = Integer.MAX_VALUE;
            for(Cluster cluster:clusterSet){
                double tmp_dis = Math.min(disC.getEuclideanDis(point,cluster.getCenter()),min_dis);
                if(tmp_dis!=min_dis){
                    min_dis = tmp_dis;
                    point.setClusterId(cluster.getId());
                    point.setDist(min_dis);
                }
            }
        }
        //消除原来所有的类中成员，把所有的点加入每一个类别
        for(Cluster cluster:clusterSet){
            cluster.getMembers().clear();
            for(Point point:pointList){
                if(point.getClusterid() == cluster.getId()){
                    cluster.addPoint(point);
                }
            }
        }
    }

    /**
     * 计算每个中心点的位置
     */
    public boolean calculateCenter(Set<Cluster> clusterSet){
        boolean ifNeedIter = false;
        for(Cluster cluster:clusterSet){
            List<Point> point_list = cluster.getMembers();
            double[] sumAll = new double[len];
            //所有点，对应各个维度求和
            for(int i=0;i<len;i++){
                for(int j=0;j<point_list.size();j++){
                    sumAll[i] += point_list.get(j).getlocalArray()[i];
                }
            }
            //计算平均值
            for(int i=0;i<sumAll.length;i++){
                sumAll[i] = sumAll[i]/point_list.size();
            }
            //计算两个新、旧中心的距离，如果任意一个类中心移动的距离大于dis_diff继续迭代
            if(disC.getEuclideanDis(cluster.getCenter(),new Point(sumAll))>disdiff)
                ifNeedIter = true;
            //设置新的类中心坐标
            cluster.setCenter(new Point(sumAll));
        }
        return ifNeedIter;
    }

    public Set<Cluster> run(){
        Set<Cluster> clusterSet = chooseCenterCluster();
        boolean ifNeedIter = true;
        while (ifNeedIter){
            cluster(clusterSet);
            ifNeedIter = calculateCenter(clusterSet);
            iterRunTimes++;
        }
        return clusterSet;
    }

    //返回实际运行次数
    public int getIterRunTimes(){
        return iterRunTimes;
    }
}
