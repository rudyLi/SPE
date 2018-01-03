package com.lifeng.query.operator;

import com.lifeng.tuple.Tuple;

public class Limit implements OperatorIterator {

    private OperatorIterator parent;
    private int limit;

    public Limit(OperatorIterator parent, int limit) {
        this.parent = parent;
        this.limit = limit;
    }

    @Override
    public void open() throws Exception {

    }
    @Override
    public Tuple next() throws Exception {
        if (limit > 0) {
            limit--;
            return parent.next();
        }
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
