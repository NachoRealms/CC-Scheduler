package cn.chengzhiya.mhdfscheduler.scheduler.impl;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdfscheduler.scheduler.SchedulerType;
import cn.chengzhiya.mhdfscheduler.task.MHDFTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public record EntityScheduler(MHDFScheduler mhdfScheduler) {
    @SuppressWarnings("unchecked")
    public MHDFTask handle(JavaPlugin plugin, Entity entity, Object task, long delay, long period) {
        MHDFTask mhdfTask = new MHDFTask();

        Runnable runnable;
        if (task instanceof Runnable r) runnable = r;
        else if (task instanceof Consumer<?> consumer)
            runnable = () -> ((Consumer<MHDFTask>) consumer).accept(mhdfTask);
        else throw new IllegalArgumentException("task needs to be a Runnable or a Consumer");

        SchedulerType schedulerType = SchedulerType.formDelayAndPeriod(delay, period);
        if (!this.mhdfScheduler.isFolia()) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            mhdfTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.runTask(plugin, runnable);
                case DELAY_RUN -> scheduler.runTaskLater(plugin, runnable, delay);
                case TASK_RUN -> scheduler.runTaskTimer(plugin, runnable, delay, period);
            });
        } else {
            io.papermc.paper.threadedregions.scheduler.EntityScheduler scheduler = entity.getScheduler();
            mhdfTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, (o) -> runnable.run(), () -> {
                });
                case DELAY_RUN -> scheduler.runDelayed(plugin, (o) -> runnable.run(), () -> {
                }, delay);
                case TASK_RUN -> scheduler.runAtFixedRate(plugin, (o) -> runnable.run(), () -> {
                }, delay, period);
            });
        }

        return mhdfTask;
    }

    public MHDFTask runTask(JavaPlugin plugin, Entity entity, Runnable runnable) {
        return this.handle(plugin, entity, runnable, 0L, 0L);
    }

    public MHDFTask runTask(JavaPlugin plugin, Entity entity, Consumer<MHDFTask> consumer) {
        return this.handle(plugin, entity, consumer, 0L, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, Entity entity, Runnable runnable, long delay) {
        return this.handle(plugin, entity, runnable, delay, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, Entity entity, Consumer<MHDFTask> consumer, long delay) {
        return this.handle(plugin, entity, consumer, delay, 0L);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, Entity entity, Runnable runnable, long delay, long period) {
        return this.handle(plugin, entity, runnable, delay, period);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, Entity entity, Consumer<MHDFTask> consumer, long delay, long period) {
        return this.handle(plugin, entity, consumer, delay, period);
    }
}
