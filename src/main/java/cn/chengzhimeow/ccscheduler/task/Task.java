package cn.chengzhimeow.ccscheduler.task;

import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public interface Task {
    void setTaskHandle(Object taskHandle);

    Plugin getOwner();

    boolean isCancelled();

    void cancel();

    int getTaskId();

    boolean isFolia();
}
