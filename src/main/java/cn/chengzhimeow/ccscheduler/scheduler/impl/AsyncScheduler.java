package cn.chengzhimeow.ccscheduler.scheduler.impl;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.SchedulerType;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class AsyncScheduler {
    private final CCScheduler ccScheduler;
    private final Object schedulerHandle;

    public AsyncScheduler(CCScheduler ccScheduler) {
        this.ccScheduler = ccScheduler;
        if (this.ccScheduler.isFolia()) this.schedulerHandle = Bukkit.getAsyncScheduler();
        else this.schedulerHandle = Bukkit.getScheduler();
    }

    @SuppressWarnings("unchecked")
    public CCTask handle(JavaPlugin plugin, Object task, long delay, long period) {
        CCTask ccTask = new CCTask(this.ccScheduler);

        Runnable runnable;
        if (task instanceof Runnable r) runnable = r;
        else if (task instanceof Consumer<?> consumer)
            runnable = () -> ((Consumer<CCTask>) consumer).accept(ccTask);
        else throw new IllegalArgumentException("task needs to be a Runnable or a Consumer");

        SchedulerType schedulerType = SchedulerType.formDelayAndPeriod(delay, period);
        if (!this.ccScheduler.isFolia()) {
            BukkitScheduler scheduler = (BukkitScheduler) this.schedulerHandle;
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.runTaskAsynchronously(plugin, runnable);
                case DELAY_RUN -> scheduler.runTaskLaterAsynchronously(plugin, runnable, delay);
                case TASK_RUN -> scheduler.runTaskTimerAsynchronously(plugin, runnable, delay, period);
            });
        } else {
            io.papermc.paper.threadedregions.scheduler.AsyncScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.AsyncScheduler) this.schedulerHandle;
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.runNow(plugin, (o) -> runnable.run());
                case DELAY_RUN ->
                        scheduler.runDelayed(plugin, (o) -> runnable.run(), delay * 50, TimeUnit.MILLISECONDS);
                case TASK_RUN ->
                        scheduler.runAtFixedRate(plugin, (o) -> runnable.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS);
            });
        }

        return ccTask;
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!this.ccScheduler.isFolia()) {
            BukkitScheduler scheduler = (BukkitScheduler) this.schedulerHandle;
            scheduler.cancelTasks(plugin);
        } else {
            io.papermc.paper.threadedregions.scheduler.AsyncScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.AsyncScheduler) this.schedulerHandle;
            scheduler.cancelTasks(plugin);
        }
    }

    public CCTask runTask(JavaPlugin plugin, Runnable runnable) {
        return this.handle(plugin, runnable, -1L, -1L);
    }

    public CCTask runTask(JavaPlugin plugin, Consumer<CCTask> consumer) {
        return this.handle(plugin, consumer, -1L, -1L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, Runnable runnable, long delay) {
        return this.handle(plugin, runnable, delay, -1L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, Consumer<CCTask> consumer, long delay) {
        return this.handle(plugin, consumer, delay, -1L);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        return this.handle(plugin, runnable, delay, period);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, Consumer<CCTask> consumer, long delay, long period) {
        return this.handle(plugin, consumer, delay, period);
    }
}
