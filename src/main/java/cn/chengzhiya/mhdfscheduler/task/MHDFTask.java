package cn.chengzhiya.mhdfscheduler.task;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class MHDFTask implements Task {
    private BukkitTask bukkitTask;
    private ScheduledTask scheduledTask;

    public MHDFTask(@NotNull BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    public MHDFTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @Override
    public Plugin getOwner() {
        return bukkitTask != null ? bukkitTask.getOwner() : scheduledTask.getOwningPlugin();
    }

    @Override
    public boolean isCancelled() {
        return bukkitTask != null ? bukkitTask.isCancelled() : scheduledTask.isCancelled();
    }

    @Override
    public void cancel() {
        if (bukkitTask != null) {
            bukkitTask.cancel();
        } else {
            scheduledTask.cancel();
        }
    }
}