package com.lifeng.task;

import com.lifeng.tuple.Tuple;

import java.util.Queue;

public class InnerMessage {
    Queue  destQueue;
    Tuple tuple;
    InnerMessage(Tuple tuple, Queue queue) {
        this.tuple = tuple;
        this.destQueue = queue;
    }

}
