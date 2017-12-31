package com.lifeng.query.operator;

public interface OperatorIterator {
    public void open() throws Exception;

    public void next() throws Exception;

    public void close() throws Exception;
}
