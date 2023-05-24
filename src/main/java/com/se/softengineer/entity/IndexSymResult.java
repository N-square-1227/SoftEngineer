package com.se.softengineer.entity;

import lombok.Data;
import net.sf.jsqlparser.statement.create.table.Index;

@Data
public class IndexSymResult implements Comparable<IndexSymResult>{
    private Double value;
    private int sampleId;

    public IndexSymResult() {
    }

    public IndexSymResult(double value, int sampleId) {
        this.value = value;
        this.sampleId = sampleId;
    }

    public int compareTo(IndexSymResult o) {
        return this.value.compareTo(o.value);
    }
}
