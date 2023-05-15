package com.se.softengineer.service;

import com.se.softengineer.entity.IndexSym;

public interface OptimizeService {

    boolean entropy(String indexsym_name, String data_tablename);

    IndexSym pca(String indexsym_name, String data_tablename);

    boolean kmeans(String indexsym_name, String data_tablename) throws Exception;
}
