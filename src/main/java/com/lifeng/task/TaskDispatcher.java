package com.lifeng.task;

import com.lifeng.common.Configuration;
import com.lifeng.network.StreamProcessResultQueue;
import com.lifeng.stream.TopologyGraph;
import com.lifeng.tuple.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class TaskDispatcher{
    private static final Map<Integer, BlockingQueue<InnerMessage>> taskQueueMap = new HashMap<>();
    private TopologyGraph topologyGraph;
    private Configuration configuration;

    TaskDispatcher(TopologyGraph topologyGraph, Configuration configuration) {
        this.topologyGraph = topologyGraph;
        this.configuration = configuration;
    }

    void init() {
        for(Task task: topologyGraph.getAllTasks()) {
            taskQueueMap.put(task.getTaskId(), new LinkedBlockingDeque<InnerMessage>());
        }

        // start netty service
    }

    List<Task> getAllTasks() {
        return topologyGraph.getAllTasks();
    }

    void finishProcessEvent(int taskId, InnerMessage innerMessage){
        List<Integer> childList = topologyGraph.getChildTasks(taskId);
        // send result to the client
        if (childList.isEmpty()) {
            try {
                innerMessage.resultQueue.addNewTask(innerMessage.tuple);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            for (int childTask : childList) {
                taskQueueMap.get(childTask).add(innerMessage);
            }
        }
    }

    InnerMessage getNewEvent(int taskId, Long time, TimeUnit timeUnit) throws InterruptedException {
        return taskQueueMap.get(taskId).poll(time, timeUnit);
    }

    public void publish(Tuple tuple, StreamProcessResultQueue resultQueue) {
        InnerMessage innerMessage = new InnerMessage(tuple, resultQueue);
        taskQueueMap.get(topologyGraph.getRootTaskId()).add(innerMessage);
    }

    public void stop() {

    }
}
