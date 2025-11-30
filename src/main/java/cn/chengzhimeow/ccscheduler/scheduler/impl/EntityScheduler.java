package cn.chengzhimeow.ccscheduler.scheduler.impl;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.SchedulerType;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public record EntityScheduler(@NotNull CCScheduler ccScheduler) {

    @SuppressWarnings("unchecked")
    public @NotNull CCTask handle(@NotNull JavaPlugin plugin, @NotNull Entity entity, @Nullable Long delay, @Nullable Long period, @NotNull Object task) {
        CCTask ccTask = new CCTask(this.ccScheduler);

        Runnable runnable;
        if (task instanceof Runnable r) runnable = r;
        else if (task instanceof Consumer<?> consumer)
            // noinspection unchecked
            runnable = () -> ((Consumer<CCTask>) consumer).accept(ccTask);
        else throw new IllegalArgumentException("task needs to be a Runnable or a Consumer");

        SchedulerType schedulerType = SchedulerType.formDelayAndPeriod(delay, period);
        if (!this.ccScheduler.isFolia()) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.runTask(plugin, runnable);
                case DELAY_RUN -> // noinspection DataFlowIssue
                        scheduler.runTaskLater(plugin, runnable, delay);
                case TASK_RUN -> // noinspection DataFlowIssue
                        scheduler.runTaskTimer(plugin, runnable, delay, period);
            });
        } else {
            io.papermc.paper.threadedregions.scheduler.EntityScheduler scheduler = entity.getScheduler();
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, (o) -> runnable.run(), () -> {
                });
                case DELAY_RUN -> // noinspection DataFlowIssue
                        scheduler.runDelayed(plugin, (o) -> runnable.run(), () -> {
                        }, delay);
                case TASK_RUN -> // noinspection DataFlowIssue
                        scheduler.runAtFixedRate(plugin, (o) -> runnable.run(), () -> {
                        }, delay, period);
            });
        }

        return ccTask;
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable runnable) {
        return this.handle(plugin, entity, null, null, runnable);
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, entity, null, null, consumer);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Entity entity, long delay, @NotNull Runnable runnable) {
        return this.handle(plugin, entity, delay, null, runnable);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Entity entity, long delay, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, entity, delay, null, consumer);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable runnable, long delay) {
        return this.runTaskLater(plugin, entity, delay, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Consumer<CCTask> consumer, long delay) {
        return this.runTaskLater(plugin, entity, delay, consumer);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Entity entity, long delay, long period, @NotNull Runnable runnable) {
        return this.handle(plugin, entity, delay, period, runnable);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Entity entity, long delay, long period, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, entity, delay, period, consumer);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, entity, delay, period, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Consumer<CCTask> consumer, long delay, long period) {
        return this.runTaskTimer(plugin, entity, delay, period, consumer);
    }
}
