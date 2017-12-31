package com.lifeng.query;

import com.lifeng.query.operator.OperatorIterator;
import com.lifeng.tuple.Tuple;

public class QueryPlan {
    private OperatorIterator headOperator;

    public void prepare() {

    }

    public Tuple next () {
        return new Tuple();
    }

    public void close() {

    }
}
