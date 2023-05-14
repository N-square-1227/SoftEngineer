package com.se.softengineer.algorithm.Kmeans;

import com.se.softengineer.algorithm.indexsym.Data;
import com.se.softengineer.algorithm.indexsym.IndexSym;
import com.se.softengineer.algorithm.indexsym.Node;
import com.se.softengineer.algorithm.trydatabase.TestMySQL;

import java.util.List;
import java.util.Set;

/**
 * @author xly
 */
public class Implement {

    public void run() throws Exception {
        ElbowMethod elbowMethod = new ElbowMethod();
        TestMySQL testMySQL = new TestMySQL();
        List<List<Double>> dataSet = testMySQL.queryData("data");
//        IndexSym indexSym = new IndexSym(testMySQL.querySym("IndexSym"));
        Data data = new Data(dataSet);
        int maxk = data.getSample_num()/2;
        System.out.println(data.getSample_num());
        int k=elbowMethod.getOptimalK(maxk);
        System.out.println(k);
        Kmeans kRun =new Kmeans(k, data);
        Set<Cluster> clusterSet = kRun.run();
        System.out.println("单次迭代运行次数："+kRun.getIterRunTimes());
        int i=1;
        for (Cluster cluster : clusterSet) {
            Node fatherNode = new Node(i,"father"+i,1,1.0,0);
            System.out.println(cluster);
//            Point center = cluster.getCenter();
            List<Point> points = cluster.getMembers();
        }
    }

    public static void main(String[] args) throws Exception {
        Implement implement = new Implement();
        implement.run();
    }
}
