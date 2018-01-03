package com.lifeng.tuple;

import java.util.ArrayList;
import java.util.List;

public class Tuple {
    private TupleDesc tupleDesc;
    private List<String> values = new ArrayList<>();

    public TupleDesc getTupleDesc() {
        return tupleDesc;
    }

    public Tuple(TupleDesc desc) {
        this.tupleDesc = desc;
    }

    public void addValue(String ... values) {
        for (String value : values) {
            this.values.add(value);
        }
    }

    public List<String> getValues() {
        return values;
    }

}
