package com.se.softengineer.service;

import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymResult;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Map;

public interface OptimizeService {

    Pair<IndexSym,Map<String, List<Object>>> entropy(String indexsym_name, String data_tablename);

    Map<String, Object> pca(String indexsym_name, String data_tablename);

    IndexSym kmeans(String indexsym_name, String data_tablename,List<Double> sllList) throws Exception;

    List<IndexSymResult> caculateResult(String dataName, String indexName, String newindexName);
}
