package com.se.softengineer.service;

import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymResult;

import java.util.List;
import java.util.TreeMap;

public interface OptimizeService {

    IndexSym entropy(String indexsym_name, String data_tablename);

    IndexSym pca(String indexsym_name, String data_tablename);

    IndexSym kmeans(String indexsym_name, String data_tablename) throws Exception;

    List<IndexSymResult> caculateResult(String dataName, String indexName, String newindexName);
}
