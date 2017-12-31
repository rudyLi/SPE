package com.lifeng.stream;

import com.lifeng.common.Configuration;
import com.lifeng.task.JobManager;

public class Topology {
    private JobManager jobManager;
    public Topology(TopologyGraph topologyGraph, Configuration configuration) {
        this.jobManager = new JobManager(topologyGraph, configuration);
    }

    public void start() {
        jobManager.start();
    }
}
