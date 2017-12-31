package com.lifeng.task;

import com.lifeng.stream.Action;
import com.lifeng.tuple.Tuple;

import java.util.concurrent.TimeUnit;

public class Task implements Runnable{
    private int taskId;
    private Action action;
    private TaskDispatcher dispatcher;
    private volatile boolean cancelled = false;

    public Task(int taskId, Action action) {
        this.taskId = taskId;
        this.action = action;
    }
    void register(TaskDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (!cancelled) {
            try {
                InnerMessage event = dispatcher.getNewEvent(taskId, 1l, TimeUnit.SECONDS);
                event.tuple = action.exec(event.tuple);
                dispatcher.finishProcessEvent(taskId, event);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public int getTaskId() {
        return taskId;
    }

    public void stop() {
        cancelled = true;
    }
}
