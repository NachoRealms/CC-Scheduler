package cn.chengzhiya.mhdfscheduler.scheduler;

import cn.chengzhiya.mhdfscheduler.task.MHDFTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class AsyncScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.AsyncScheduler asyncScheduler;

    public AsyncScheduler() {
        if (MHDFScheduler.isFolia()) {
            asyncScheduler = Bukkit.getAsyncScheduler();
        } else {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public MHDFTask runTask(@NotNull Plugin plugin, @NotNull Runnable task) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskAsynchronously(plugin, task));
        }

        return new MHDFTask(asyncScheduler.runNow(plugin, (o) -> task.run()));
    }

    public MHDFTask runTaskLater(@NotNull Plugin plugin, @NotNull Runnable task, long delay) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskLaterAsynchronously(plugin, task, delay));
        }

        return new MHDFTask(asyncScheduler.runDelayed(plugin, (o) -> task.run(), delay * 50, TimeUnit.MILLISECONDS));
    }

    public MHDFTask runTaskTimer(@NotNull Plugin plugin, @NotNull Runnable task, long delay, long period) {
        if (period < 1) period = 1;

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskTimerAsynchronously(plugin, task, delay, period));
        }

        return new MHDFTask(asyncScheduler.runAtFixedRate(plugin, (o) -> task.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS));
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!MHDFScheduler.isFolia()) {
            bukkitScheduler.cancelTasks(plugin);
            return;
        }

        asyncScheduler.cancelTasks(plugin);
    }
}