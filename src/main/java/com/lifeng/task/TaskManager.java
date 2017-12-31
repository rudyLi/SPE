package com.lifeng.task;


import com.lifeng.common.Configuration;

import java.util.concurrent.ExecutorService;

public class TaskManager {
    private Task task;

    TaskManager(Task task) {
        this.task = task;
    }

    void register(TaskDispatcher taskDispatcher) {
        task.register(taskDispatcher);
    }

    void start(ExecutorService executorService) {
        executorService.submit(task);
    }

    void stop() {
        task.stop();
    }


}
