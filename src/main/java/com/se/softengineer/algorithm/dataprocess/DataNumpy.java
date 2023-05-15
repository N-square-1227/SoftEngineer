package com.se.softengineer.algorithm.dataprocess;

import com.se.softengineer.algorithm.indexsym.Data;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * 还没写完私密马赛
 * 处理数据的类，定义的都是static的方法
 * 做一些类似求方差啦求平均值啦之类的事情
 */
public class DataNumpy {

    /**
     * 单拎出来写了个函数，感觉用的还挺多
     **/
    public static double[][] data_matrix(List<List<Double>> data, int row_num, int col_num) {
        /* 二维数组比较好还是List比较好我也不知道 */
        double[][] data_matrix = new double[row_num][col_num];

        /* 先放到二维数组里存起来，二维数据方便就方便在可以下标遍历 */
        for(int i = 0; i < row_num; i ++)
            for(int j = 0; j < col_num; j ++)
                data_matrix[i][j] = data.get(i).get(j);

        return data_matrix;
    }

    /**
     * 标准化
     * 不用转置好像，直接按照读进来的放到二维数组里，一列是一个指标变量
     * 那个线性代数的就是求一列的的均值方差啥的
     * 一会转置一会不转置的，要不还是数组存吧
     **/
    public static double[][] normalize(List<List<Double>> data) {
        int row_num = data.size();       // 行数，就是第一重List的size，未转之前应该是sample_num
        int col_num;
        try {
            col_num = data.get(0).size(); // 列数，就是第二重List中元素的个数，未转置前应该是index_num
        }
        catch (IndexOutOfBoundsException e) {
            row_num = 0;
            col_num = 0;
        }
        double[][] data_matrix = data_matrix(data, row_num, col_num);

        RealMatrix matrix = new Array2DRowRealMatrix(data_matrix);
        int numRows = matrix.getRowDimension();
        int numCols = matrix.getColumnDimension();

        /* 计算每个特征列的均值和标准差 */
        double[] means = new double[numCols];
        double[] stdDevs = new double[numCols];
        Mean meanCalculator = new Mean();
        StandardDeviation stdDevCalculator = new StandardDeviation();

        for (int j = 0; j < numCols; j++) {
            double[] col = matrix.getColumn(j);
            means[j] = meanCalculator.evaluate(col);
            stdDevs[j] = stdDevCalculator.evaluate(col, means[j]);
        }

        RealMatrix standardizedMatrix = matrix.copy();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double val = standardizedMatrix.getEntry(i, j);
                if(stdDevs[j] == 0) standardizedMatrix.setEntry(i, j, 0);
                else standardizedMatrix.setEntry(i, j, (val - means[j]) / stdDevs[j]);
            }
        }

        return standardizedMatrix.getData();
    }

    /* 从数据库里读来的数据List<List>里面一个List是一个样本
     * 想改成里面的一个List一个指标方便处理
     * 就是对矩阵做转置(除了遍历好像没什么简单的办法了),只能做O(mn)的
     * 有好的方法跟我说一声
     **/
    public static void transposition(List<List<Double>> data) {
        // Java里没有很明显的方法区分值传参还是引用传参
        // 但是据说自定义Object的传参是引用传参, 所以试一下

        /* 二维数组比较好还是List比较好我也不知道 */
        /* 先放到二维数组里存起来，二维数据方便就方便在可以下标遍历 */
        int row_num = data.size();       // 行数，就是第一重List的size，未转之前应该是sample_num
        int col_num;
        try {
            col_num = data.get(0).size(); // 列数，就是第二重List中元素的个数，未转置前应该是index_num
        }
        catch (IndexOutOfBoundsException e) {
            row_num = 0;
            col_num = 0;
        }
        double[][] data_matrix = data_matrix(data, row_num, col_num);

        data.clear();

        List<List<Double>> new_data = new ArrayList<>();

        /* 把data的List<List> 变成里面的List是指标，外面的List是样本 */
        for(int j = 0; j < col_num; j ++) {
            List<Double> sample = new ArrayList<>();
            for(int i = 0; i < row_num; i ++){
                sample.add(data_matrix[i][j]);
            }
            new_data.add(sample);
        }
        data.addAll(new_data);
        /**
         * 意思就是这个意思，流程就是先把原来的数据存到二维数组里（方便行列转换直接取下标）
         * 然后把原本的清空
         * 转置之后再把新的数据set到对象里
         *
         * 问题在于：
         * 1. 希望的是调用这个函数的时候，Data.transposition(data)，这一行运行完之后data里的List<List>就被转置了
         * 2. 原本的List<List>被清空了, 不知道是不是保存了capacity信息，可能会有点问题
         */
    }

    /**
     * 求平均值向量
     * 对于data中的List<List>
     * aveList.get(i)=ave(List.get(i))
     * 也就是说在data被转置之后，List.get(i)获取到的是第i个指标变量在多个样本取值的List
     * 那么aveList.get(i)就是每个指标变量值的平均值
     *
     * 如果data没有经过转置，也就是说List.get(i)获取到的是第i个样本
     * 那aveList.get(i)计算得到第i个样本多个指标值的list
     **/
    public static List<Double> average_vector(Data data) {
        List<Double> aveList = new ArrayList<>();

        /* 反正是求嵌套的内部List的平均值 */
        int row_num = data.getData().size();
        int col_num;
        try {
            col_num = data.getData().get(0).size();
        }
        catch (IndexOutOfBoundsException e) {
            row_num = 0;
            col_num = 0;
        }
        for(int i = 0; i <= row_num; i ++) {
            Double sum = 0.0;
            for(int j = 0; j <= col_num; j ++) {
                sum += data.getData().get(i).get(j);
            }
            if(col_num == 0) aveList.add(0.0);
            else aveList.add(sum/col_num);
        }
        return aveList;
    }

    /**
     * 求单个序列的平均值
     **/
    public static Double average(List<Double> valueList) {
        Double sum = 0.0;
        int num = valueList.size();
        for(int j = 0; j <= num; j ++) {
            sum += valueList.get(j);
        }
        if(num == 0) return 0.0;
        else return sum/num;
    }

    /**
     * 求平方根的函数
     * 我才知道原来Java里也有vector
     **/
    public static Double distance(List<Double> v1, List<Double> v2) {
        double d = 0.0;
        int v_num = v1.size();
        if(v2.size() != v_num) {
            /* 看看在Web怎么处理 */
            System.out.println("Error: 数量不匹配!");
        }

        for(int i = 0; i < v_num; i++)
            d += (v1.get(i) - v2.get(i)) * (v1.get(i) - v2.get(i));

        return Math.sqrt(d);
    }

    /**
     * 求协方差矩阵
     * Java有库，太好了
     * 这个线性代数库好好用，可以直接做矩阵
     * ！！！！后面可能要考虑改数据结构！！！！！！
     */
    public static double[][] conv_matrix(double[][] data) {
        /* 调包算协方差矩阵*/
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RealMatrix covarianceMatrix = new Covariance(matrix).getCovarianceMatrix();

//        System.out.println(covarianceMatrix);
        return covarianceMatrix.getData();
    }

    /**
     * 求解了特征值和特征向量
     * 只返回了特征值，看后续算法操作再看返回什么
     * 怎么不早跟我说线性代数有库，有库的话List<List>可就不太好用了
     **/
    public static EigenDecomposition eigen(double[][] data) {

        /* 求解特征值和特征向量 */
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        EigenDecomposition decomposition = new EigenDecomposition(matrix);

        /* 获取特征值和特征向量 */
        /*double[] eigenvalues = decomposition.getRealEigenvalues();
        RealMatrix eigenvectors = decomposition.getV();*/

        return decomposition;
    }

}
