package cn.chengzhimeow.ccscheduler.task;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class CCTask implements Task {
    private final CCScheduler ccScheduler;
    private Object taskHandle;
    @Getter
    private boolean folia;

    public CCTask(CCScheduler ccScheduler) {
        this.ccScheduler = ccScheduler;
    }

    public void setTaskHandle(Object taskHandle) {
        if (this.ccScheduler.isFolia()) {
            this.folia = taskHandle instanceof ScheduledTask;
        } else this.folia = false;
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