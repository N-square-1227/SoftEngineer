package com.se.softengineer.service;

import com.se.softengineer.entity.IndexSym;

public interface OptimizeService {

    IndexSym entropy(String indexsym_name, String data_tablename);

    IndexSym pca(String indexsym_name, String data_tablename);

    IndexSym kmeans(String indexsym_name, String data_tablename) throws Exception;
}
