package com.lifeng.task;

import com.lifeng.common.Configuration;
import com.lifeng.stream.TopologyGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobManager {
    private TaskDispatcher taskDispatcher;
    private List<TaskManager> taskList = new LinkedList<>();
    private ExecutorService executorService;

    public JobManager(TopologyGraph graph, Configuration configuration) {
       this.taskDispatcher = new TaskDispatcher(graph, configuration);
    }

    private void init() {
        taskDispatcher.init();
        executorService = Executors.newFixedThreadPool(taskDispatcher.getAllTasks().size());
        for (Task task : taskDispatcher.getAllTasks()){
            TaskManager taskManager = new TaskManager(task);
            taskManager.register(taskDispatcher);
            taskList.add(taskManager);
        }
    }
    public void start() {
        init();
        for (TaskManager taskManager : taskList) {
            taskManager.start(executorService);
        }
    }

    public void stop() {
        for(TaskManager taskManager : taskList) {
            taskManager.stop();
        }
        executorService.shutdown();
        taskDispatcher.stop();
    }


}
