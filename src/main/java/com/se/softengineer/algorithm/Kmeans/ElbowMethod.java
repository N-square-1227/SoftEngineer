package com.se.softengineer.algorithm.Kmeans;

import com.se.softengineer.entity.Sample;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.List;
import java.util.Set;

/**
 * @author xly
 * 利用手肘法求出最优K值
 */
public class ElbowMethod {

    public double[] getSSl(int maxK, List<Sample> sampleList){
        double[] wssList = new double[maxK];
        for (int k = 1; k <= maxK; k++) {
            Kmeans kRun = new Kmeans(k,sampleList);
            Set<Cluster> clusterSet = kRun.run();
            double wss = 0.0;
            for (Cluster cluster:clusterSet) {
                double[] center = cluster.getCenter().getlocalArray();
                List<Point> point_list = cluster.getMembers();
                for(int i=0;i<point_list.size();i++){
                    double[] point = point_list.get(i).getlocalArray();
                    wss += Math.pow(new EuclideanDistance().compute(point, center), 2.0);
                }
            }
            wssList[k - 1] = wss;
            System.out.println(k-1+":"+wss);
        }
        return wssList;
    }

    public int getOptimalK(double[] wssList) throws Exception {
        // 应用手肘法求出最优的k值
        int optimalK = 1;
        double maxDist = 0.0;
        for (int k = 1; k <= wssList.length-1; k++) {
            double dist = Math.abs(wssList[k] - wssList[k - 1]);
            System.out.println(dist);
            if (dist > maxDist) {
                maxDist = dist;
                optimalK = k + 1;
            }
        }
        return optimalK;
    }
}
