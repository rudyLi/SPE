package com.lifeng.query.operator;

import com.lifeng.tuple.Tuple;

public interface Filter {
    public boolean accept(Tuple tuple);
}
