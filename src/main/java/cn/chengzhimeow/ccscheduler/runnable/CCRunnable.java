package cn.chengzhimeow.ccscheduler.runnable;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
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
        this.ccTask = this.ccScheduler.getGlobalRegionScheduler().runTaskLater(plugin, this::run, delay);
    }

    @Override
    public void runTaskTimer(JavaPlugin plugin, long delay, long period) {
        this.ccTask = this.ccScheduler.getGlobalRegionScheduler().runTaskTimer(plugin, this::run, delay, period);
    }

    @Override
    public void runTaskAsynchronously(JavaPlugin plugin) {
        this.ccTask = this.ccScheduler.getAsyncScheduler().runTask(plugin, this::run);
    }

    @Override
    public void runTaskLaterAsynchronously(JavaPlugin plugin, long delay) {
        this.ccTask = this.ccScheduler.getAsyncScheduler().runTaskLater(plugin, this::run, delay);
    }

    @Override
    public void runTaskTimerAsynchronously(JavaPlugin plugin, long delay, long period) {
        this.ccTask = this.ccScheduler.getAsyncScheduler().runTaskTimer(plugin, this::run, delay, period);
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
}
