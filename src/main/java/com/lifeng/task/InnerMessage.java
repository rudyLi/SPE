package com.lifeng.task;

import com.lifeng.network.StreamProcessResultQueue;
import com.lifeng.tuple.Tuple;

public class InnerMessage {
    StreamProcessResultQueue resultQueue;
    Tuple tuple;
    InnerMessage(Tuple tuple, StreamProcessResultQueue queue) {
        this.tuple = tuple;
        this.resultQueue = queue;
    }

}
