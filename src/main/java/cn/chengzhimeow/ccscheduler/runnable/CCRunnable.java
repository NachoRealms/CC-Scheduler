package cn.chengzhimeow.ccscheduler.runnable;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@SuppressWarnings("unused")
public abstract class CCRunnable implements Runnable {
    public CCScheduler ccScheduler;
    public CCTask ccTask;

    public CCRunnable(CCScheduler ccScheduler) {
        this.ccScheduler = ccScheduler;
    }

    @Override
    public void runTask(JavaPlugin plugin) {
        this.ccTask = this.ccScheduler.getGlobalRegionScheduler().runTask(plugin, this::run);
    }

    @Override
    public void runTaskLater(JavaPlugin plugin, long delay) {
        this.ccTask = this.ccScheduler.getGlobalRegionScheduler().runTaskLater(plugin, delay, this::run);
    }

    @Override
    public void runTaskTimer(JavaPlugin plugin, long delay, long period) {
        this.ccTask = this.ccScheduler.getGlobalRegionScheduler().runTaskTimer(plugin, delay, period, this::run);
    }

    @Override
    public void runTaskAsynchronously(JavaPlugin plugin) {
        this.ccTask = this.ccScheduler.getAsyncScheduler().runTask(plugin, this::run);
    }

    @Override
    public void runTaskLaterAsynchronously(JavaPlugin plugin, long delay) {
        this.ccTask = this.ccScheduler.getAsyncScheduler().runTaskLater(plugin, delay, this::run);
    }

    @Override
    public void runTaskTimerAsynchronously(JavaPlugin plugin, long delay, long period) {
        this.ccTask = this.ccScheduler.getAsyncScheduler().runTaskTimer(plugin, delay, period, this::run);
    }

    @Override
    public void setTaskHandle(Object taskHandle) {
        throw new UnsupportedOperationException("不支持该方法");
    }

    @Override
    public Plugin getOwner() {
        return this.ccTask.getOwner();
    }

    @Override
    public boolean isCancelled() {
        return this.ccTask.isCancelled();
    }

    @Override
    public void cancel() {
        this.ccTask.cancel();
    }

    @Override
    public int getTaskId() {
        return this.ccTask.getTaskId();
    }

    @Override
    public boolean isFolia() {
        return this.ccTask.isFolia();
    }
}
