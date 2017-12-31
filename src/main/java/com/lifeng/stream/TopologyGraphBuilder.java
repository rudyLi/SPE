package com.lifeng.stream;

import com.lifeng.task.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TopologyGraphBuilder {
    // here use the list to store the task execute graph simply
    private List<Task> tasks = new LinkedList<>();

    public void add(Action action) {
        tasks.add(new Task(TaskIdProducer.getId(), action));
    }

    public TopologyGraph build() {
        // server handler task
        return new TopologyGraph(tasks);
    }

    private static class TaskIdProducer{
        private static AtomicInteger id =  new AtomicInteger(0);
        private static int getId() {
            return id.addAndGet(1);
        }
    }
}
