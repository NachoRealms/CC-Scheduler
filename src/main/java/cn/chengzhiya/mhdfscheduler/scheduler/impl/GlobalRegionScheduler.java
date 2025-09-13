package cn.chengzhiya.mhdfscheduler.scheduler.impl;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdfscheduler.scheduler.SchedulerType;
import cn.chengzhiya.mhdfscheduler.task.MHDFTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class GlobalRegionScheduler {
    private final MHDFScheduler mhdfScheduler;
    private final Object schedulerHandle;

    public GlobalRegionScheduler(MHDFScheduler mhdfScheduler) {
        this.mhdfScheduler = mhdfScheduler;
        if (this.mhdfScheduler.isFolia()) this.schedulerHandle = Bukkit.getGlobalRegionScheduler();
        else this.schedulerHandle = Bukkit.getScheduler();
    }

    @SuppressWarnings("unchecked")
    public MHDFTask handle(JavaPlugin plugin, Object task, long delay, long period) {
        MHDFTask mhdfTask = new MHDFTask();

        Runnable runnable;
        if (task instanceof Runnable r) runnable = r;
        else if (task instanceof Consumer<?> consumer)
            runnable = () -> ((Consumer<MHDFTask>) consumer).accept(mhdfTask);
        else throw new IllegalArgumentException("task needs to be a Runnable or a Consumer");

        SchedulerType schedulerType = SchedulerType.formDelayAndPeriod(delay, period);
        if (!this.mhdfScheduler.isFolia()) {
            BukkitScheduler scheduler = (BukkitScheduler) this.schedulerHandle;
            mhdfTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.runTask(plugin, runnable);
                case DELAY_RUN -> scheduler.runTaskLater(plugin, runnable, delay);
                case TASK_RUN -> scheduler.runTaskTimer(plugin, runnable, delay, period);
            });
        } else {
            io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler) this.schedulerHandle;
            mhdfTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, (o) -> runnable.run());
                case DELAY_RUN -> scheduler.runDelayed(plugin, (o) -> runnable.run(), delay);
                case TASK_RUN -> scheduler.runAtFixedRate(plugin, (o) -> runnable.run(), delay, period);
            });
        }

        return mhdfTask;
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!this.mhdfScheduler.isFolia()) {
            BukkitScheduler scheduler = (BukkitScheduler) this.schedulerHandle;
            scheduler.cancelTasks(plugin);
        } else {
            io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler) this.schedulerHandle;
            scheduler.cancelTasks(plugin);
        }
    }

    public MHDFTask runTask(JavaPlugin plugin, Runnable runnable) {
        return this.handle(plugin, runnable, 0L, 0L);
    }

    public MHDFTask runTask(JavaPlugin plugin, Consumer<MHDFTask> consumer) {
        return this.handle(plugin, consumer, 0L, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, Runnable runnable, long delay) {
        return this.handle(plugin, runnable, delay, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, Consumer<MHDFTask> consumer, long delay) {
        return this.handle(plugin, consumer, delay, 0L);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        return this.handle(plugin, runnable, delay, period);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, Consumer<MHDFTask> consumer, long delay, long period) {
        return this.handle(plugin, consumer, delay, period);
    }
}
