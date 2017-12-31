package com.lifeng.stream;

import com.lifeng.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopologyGraph {
    //todo the storage should change
    private List<Task> tasks = new ArrayList<>();
    private int rootTaskId;

    private Map<Integer, List<Integer>> childTaskList = new HashMap<>();
    public TopologyGraph(List<Task> tasks) {
        this.tasks = tasks;
        // here just simple to get root task
        rootTaskId = tasks.get(0).getTaskId();
        for (int i=0; i < tasks.size() -1; i++) {
            List<Integer> child = new ArrayList<>();
            child.add(tasks.get(i+1).getTaskId());
            childTaskList.put(tasks.get(0).getTaskId(), child);
        }
    }
    private Map<Integer, List<Integer>> childRelationMap = new HashMap<>();

    public int getTaskTotalNum() {
        return tasks.size();
    }

    public List<Task> getAllTasks( ) {
        return tasks;
    }

    public List<Integer> getChildTasks(int taskId) {
        return childRelationMap.get(taskId);
    }

    public int getRootTaskId() {
        return rootTaskId;
    }
}
