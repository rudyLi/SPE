package com.lifeng.query.operator;

import com.lifeng.tuple.Tuple;

public interface OperatorIterator {
    // do some prepare job for this task
    public void open() throws Exception;

    public Tuple next() throws Exception;

    public void close() throws Exception;
}
