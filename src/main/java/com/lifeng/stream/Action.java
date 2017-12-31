package com.lifeng.stream;

import com.lifeng.tuple.Tuple;

public interface Action {
    public Tuple exec(Tuple tuple);
}
