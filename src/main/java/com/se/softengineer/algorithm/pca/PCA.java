package com.se.softengineer.algorithm.pca;


import com.se.softengineer.algorithm.dataprocess.DataNumpy;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.Sample;
import lombok.Data;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;

@Data
public class PCA {

    private List<Sample> data;

    private IndexSym new_sym;

    private double[][] load_matrix;

    private double threshold = 0.7;

    private int factor_num;

    private String indexsym_name;

    public List<Sample> getData() {
        return data;
    }

    public  PCA() {
        data = new ArrayList<>();
        new_sym = new IndexSym();
        factor_num = 0;
    }

    public PCA(List<Sample> data, String indexsym_name) {
        this.data = data;
        this.new_sym = new IndexSym();
        factor_num = 0;
        this.indexsym_name = indexsym_name;
    }

    public boolean solve() {

        /**
         * 前面可能应该还要数据中心化（再说）
         **/

        /* 先标准化，标准化那个线性代数的包就是求一列一列的均值和方差，所以不用转置 */
        RealMatrix normalizedMatrix = new Array2DRowRealMatrix(DataNumpy.normalize(data));

        /* 协方差矩阵 */
        double[][] conv_matrix = DataNumpy.conv_matrix(normalizedMatrix.getData());
//        System.out.println(conv_matrix.length);


        /* 特征值和特征向量 */
        EigenDecomposition decomposition = DataNumpy.eigen(conv_matrix);
        double[] eigenvalued = decomposition.getRealEigenvalues();  // 特征值
        RealMatrix eigenvectors = decomposition.getV();             // 特征向量
        /* 默认的特征值就是降序排好的，特征向量是对应的 */

        /* 算累积贡献率 */
        int numCols = eigenvalued.length;
        double[] cumu_contri = new double[numCols];
        double sum = Arrays.stream(eigenvalued).sum();
        cumu_contri[0] = eigenvalued[0] / sum;
        factor_num = 0;       // 标记累计贡献率超过85%的地方
        for(int i = 1; i < numCols; i ++) {
            cumu_contri[i] = 0.0;
            cumu_contri[i] = cumu_contri[i - 1] + eigenvalued[i] / sum;
            factor_num ++;
            if(cumu_contri[i] >= 0.85)
                break;
        }

        // 截取前n个作为主成分，求因子载荷矩阵
        double[][] eigenMatrix = eigenvectors.getSubMatrix(0, conv_matrix.length - 1, 0, factor_num - 1).getData();
        int numRows = conv_matrix.length;
        System.out.println("");
        for(int i = 0; i < numRows; i ++) {
            for(int j = 0; j < factor_num; j ++)
                eigenMatrix[i][j] *= -Math.sqrt(eigenvalued[j]);
        }

        this.load_matrix = eigenMatrix;
//        for(int i = 0; i < numCols; i ++) {
//            for(int j = 0; j < factor_num; j ++)
//                System.out.print(eigenMatrix[i][j]);
//            System.out.println("");
//        }

        /* 先把因子加入到指标体系中 */
        /* 因子载荷矩阵一行是一个因子，和标准化矩阵的顺序一样，也就是和数据库子节点出现的顺序一样 */
        /* 对每一个主成分而言（每一列），因子载荷矩阵的值越大，说明对这个主成分的影响越大，并且对应因子载荷矩阵的值就是权值 */
        int id_no = 1;
        int factor_id = 1;
        this.indexsym_name = this.indexsym_name.substring(this.indexsym_name.indexOf('_') + 1);
        IndexSymNode root = new IndexSymNode(id_no, this.indexsym_name, 1, 1.0, 0);
        new_sym.addNode(root);
        id_no ++;
        for(int i = 0; i < factor_num; i ++) {
            IndexSymNode factor = new IndexSymNode(id_no, "factor" + factor_id, 1, 1.0, 1);
            boolean factorin = false;
            for(int j = 1; j <=numRows; j ++) {
                /* 数据不太好感觉 */
                if(Math.abs(eigenMatrix[j - 1][i]) >= threshold) {
                    if(!factorin) {
                        new_sym.addNode(factor);
                        id_no++;
                        factor_id++;
                        factorin = true;
                    }
                    /* 这里的名字先用编号，然后看在哪里换成名字 */
                    IndexSymNode node = new IndexSymNode(id_no, String.valueOf(j), 1, eigenMatrix[j - 1][i], factor.getNodeID());
                    new_sym.addNode(node);
                    id_no ++;
                }
            }
        }

        factor_num = factor_id - 1;

        /* 权重归一化 */
        List<IndexSymNode> new_nodeList = new_sym.getNodeList();
        for(IndexSymNode node : new_nodeList) {
            if ( new_sym.getNodeTree().get(node.getNodeID()).size() != 0 ) {    // 是一级指标
                List<Integer> sons = new_sym.getNodeTree().get(node.getNodeID());
                double weight_sum = 0.0;
                for(int son : sons) {
                    weight_sum += Math.abs(new_nodeList.get(son - 1).getNodeWeight());
                }
                for(int son : sons) {
                    new_nodeList.get(son - 1).setNodeWeight(new_nodeList.get(son - 1).getNodeWeight() / weight_sum);
                }
            }
        }

        return true;

        /**
         * 后面要不要判重啊
         * 就是有没有可能同一个指标对两个因子来说因子载荷矩阵里的那个值都很大
         * 一般好像不会，0.65是没有了
         */
    }
}
