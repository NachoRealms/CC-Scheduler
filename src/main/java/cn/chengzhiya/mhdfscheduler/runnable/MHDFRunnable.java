package cn.chengzhiya.mhdfscheduler.runnable;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdfscheduler.task.MHDFTask;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class MHDFRunnable implements Runnable {
    public MHDFTask mhdfTask;

    @Override
    public void runTask(JavaPlugin plugin) {
        this.mhdfTask = MHDFScheduler.getGlobalRegionScheduler().runTask(plugin, (task) -> run());
    }

    @Override
    public void runTaskLater(JavaPlugin plugin, long delay) {
        this.mhdfTask = MHDFScheduler.getGlobalRegionScheduler().runTaskLater(plugin, (task) -> run(), delay);
    }

    @Override
    public void runTaskTimer(JavaPlugin plugin, long delay, long period) {
        this.mhdfTask = MHDFScheduler.getGlobalRegionScheduler().runTaskTimer(plugin, (task) -> run(), delay, period);
    }

    @Override
    public void runTaskAsynchronously(JavaPlugin plugin) {
        this.mhdfTask = MHDFScheduler.getAsyncScheduler().runTask(plugin, (task) -> run());
    }

    @Override
    public void runTaskLaterAsynchronously(JavaPlugin plugin, long delay) {
        this.mhdfTask = MHDFScheduler.getAsyncScheduler().runTaskLater(plugin, (task) -> run(), delay);
    }

    @Override
    public void runTaskTimerAsynchronously(JavaPlugin plugin, long delay, long period) {
        this.mhdfTask = MHDFScheduler.getAsyncScheduler().runTaskTimer(plugin, (task) -> run(), delay, period);
    }

    @Override
    public Plugin getOwner() {
        return mhdfTask.getOwner();
    }

    @Override
    public boolean isCancelled() {
        return mhdfTask.isCancelled();
    }

    @Override
    public void cancel() {
        mhdfTask.cancel();
    }
}
