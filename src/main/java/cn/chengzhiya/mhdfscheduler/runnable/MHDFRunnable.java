package cn.chengzhiya.mhdfscheduler.runnable;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdfscheduler.task.MHDFTask;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class MHDFRunnable implements Runnable {
    public MHDFScheduler mhdfScheduler;
    public MHDFTask mhdfTask;

    public MHDFRunnable(MHDFScheduler mhdfScheduler) {
        this.mhdfScheduler = mhdfScheduler;
    }

    @Override
    public void runTask(JavaPlugin plugin) {
        this.mhdfTask = this.mhdfScheduler.getGlobalRegionScheduler().runTask(plugin, this::run);
    }

    @Override
    public void runTaskLater(JavaPlugin plugin, long delay) {
        this.mhdfTask = this.mhdfScheduler.getGlobalRegionScheduler().runTaskLater(plugin, this::run, delay);
    }

    @Override
    public void runTaskTimer(JavaPlugin plugin, long delay, long period) {
        this.mhdfTask = this.mhdfScheduler.getGlobalRegionScheduler().runTaskTimer(plugin, this::run, delay, period);
    }

    @Override
    public void runTaskAsynchronously(JavaPlugin plugin) {
        this.mhdfTask = this.mhdfScheduler.getAsyncScheduler().runTask(plugin, this::run);
    }

    @Override
    public void runTaskLaterAsynchronously(JavaPlugin plugin, long delay) {
        this.mhdfTask = this.mhdfScheduler.getAsyncScheduler().runTaskLater(plugin, this::run, delay);
    }

    @Override
    public void runTaskTimerAsynchronously(JavaPlugin plugin, long delay, long period) {
        this.mhdfTask = this.mhdfScheduler.getAsyncScheduler().runTaskTimer(plugin, this::run, delay, period);
    }

    @Override
    public Plugin getOwner() {
        return this.mhdfTask.getOwner();
    }

    @Override
    public boolean isCancelled() {
        return this.mhdfTask.isCancelled();
    }

    @Override
    public void cancel() {
        this.mhdfTask.cancel();
    }
}
