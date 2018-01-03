package com.lifeng.query;

import com.lifeng.tuple.Tuple;

public class Cursor {
    private QueryPlan queryPlan;

    public Cursor(QueryPlan queryPlan) {
        this.queryPlan = queryPlan;
    }
    public void open() throws Exception {
        queryPlan.open();
    }

    public Tuple next() throws Exception {
        return queryPlan.next();
    }

    public void close() throws Exception {
        queryPlan.close();
    }
}
