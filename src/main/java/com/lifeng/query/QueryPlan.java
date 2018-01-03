package com.lifeng.query;

import com.lifeng.query.operator.OperatorIterator;
import com.lifeng.tuple.Tuple;

public class QueryPlan {
    private OperatorIterator headOperator;
    public void setHeadOperator(OperatorIterator iterator) {
        this.headOperator = iterator;
    }

    public void open() throws Exception {
        headOperator.open();
    }

    public Tuple next () throws Exception {
        return headOperator.next();
    }

    public void close() throws Exception {
        headOperator.close();
    }
}
