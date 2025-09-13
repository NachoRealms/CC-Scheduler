package cn.chengzhiya.mhdfscheduler.task;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class MHDFTask implements Task {
    private Object taskHandle;
    @Getter
    private boolean folia;

    public MHDFTask() {
    }

    public MHDFTask(Object taskHandle) {
        this.folia = taskHandle instanceof ScheduledTask;
        this.taskHandle = taskHandle;
    }

    public void setTaskHandle(Object taskHandle) {
        this.folia = taskHandle instanceof ScheduledTask;
        this.taskHandle = taskHandle;
    }

    @Override
    public Plugin getOwner() {
        if (this.isFolia()) return ((ScheduledTask) this.taskHandle).getOwningPlugin();
        else return ((BukkitTask) this.taskHandle).getOwner();
    }

    @Override
    public boolean isCancelled() {
        if (this.isFolia()) return ((ScheduledTask) this.taskHandle).isCancelled();
        else return ((BukkitTask) this.taskHandle).isCancelled();
    }

    @Override
    public void cancel() {
        if (this.isFolia()) ((ScheduledTask) this.taskHandle).cancel();
        else ((BukkitTask) this.taskHandle).cancel();
    }

    @Override
    public int getTaskId() {
        if (this.isFolia()) throw new IllegalArgumentException("folia not supported getTaskId()");
        else return ((BukkitTask) this.taskHandle).getTaskId();
    }
}