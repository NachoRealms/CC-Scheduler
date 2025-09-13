package cn.chengzhimeow.ccscheduler.scheduler.impl;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.SchedulerType;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public record EntityScheduler(CCScheduler ccScheduler) {
    @SuppressWarnings("unchecked")
    public CCTask handle(JavaPlugin plugin, Entity entity, Object task, long delay, long period) {
        CCTask ccTask = new CCTask();

        Runnable runnable;
        if (task instanceof Runnable r) runnable = r;
        else if (task instanceof Consumer<?> consumer)
            runnable = () -> ((Consumer<CCTask>) consumer).accept(ccTask);
        else throw new IllegalArgumentException("task needs to be a Runnable or a Consumer");

        SchedulerType schedulerType = SchedulerType.formDelayAndPeriod(delay, period);
        if (!this.ccScheduler.isFolia()) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.runTask(plugin, runnable);
                case DELAY_RUN -> scheduler.runTaskLater(plugin, runnable, delay);
                case TASK_RUN -> scheduler.runTaskTimer(plugin, runnable, delay, period);
            });
        } else {
            io.papermc.paper.threadedregions.scheduler.EntityScheduler scheduler = entity.getScheduler();
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, (o) -> runnable.run(), () -> {
                });
                case DELAY_RUN -> scheduler.runDelayed(plugin, (o) -> runnable.run(), () -> {
                }, delay);
                case TASK_RUN -> scheduler.runAtFixedRate(plugin, (o) -> runnable.run(), () -> {
                }, delay, period);
            });
        }

        return ccTask;
    }

    public CCTask runTask(JavaPlugin plugin, Entity entity, Runnable runnable) {
        return this.handle(plugin, entity, runnable, 0L, 0L);
    }

    public CCTask runTask(JavaPlugin plugin, Entity entity, Consumer<CCTask> consumer) {
        return this.handle(plugin, entity, consumer, 0L, 0L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, Entity entity, Runnable runnable, long delay) {
        return this.handle(plugin, entity, runnable, delay, 0L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, Entity entity, Consumer<CCTask> consumer, long delay) {
        return this.handle(plugin, entity, consumer, delay, 0L);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, Entity entity, Runnable runnable, long delay, long period) {
        return this.handle(plugin, entity, runnable, delay, period);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, Entity entity, Consumer<CCTask> consumer, long delay, long period) {
        return this.handle(plugin, entity, consumer, delay, period);
    }
}
