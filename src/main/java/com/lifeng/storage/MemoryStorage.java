package com.lifeng.storage;

import com.lifeng.tuple.Tuple;


public class MemoryStorage implements Storage<Tuple> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Tuple next() {
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
