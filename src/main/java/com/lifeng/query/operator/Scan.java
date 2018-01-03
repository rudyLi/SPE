package com.lifeng.query.operator;

import com.lifeng.storage.Storage;
import com.lifeng.tuple.Tuple;

public class Scan implements OperatorIterator {

    private Storage<Tuple> storage;

    public Scan(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public Tuple next() throws Exception {
        return storage.hasNext()? storage.next() : null;
    }

    @Override
    public void close() throws Exception {

    }
}
