package cn.chengzhimeow.ccscheduler.scheduler.impl;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.SchedulerType;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class GlobalRegionScheduler {
    private final CCScheduler ccScheduler;
    private final Object schedulerHandle;

    public GlobalRegionScheduler(@NotNull CCScheduler ccScheduler) {
        this.ccScheduler = ccScheduler;
        if (this.ccScheduler.isFolia()) this.schedulerHandle = Bukkit.getGlobalRegionScheduler();
        else this.schedulerHandle = Bukkit.getScheduler();
    }

    @SuppressWarnings("unchecked")
    public @NotNull CCTask handle(@NotNull JavaPlugin plugin, @Nullable Long delay, @Nullable Long period, @NotNull Object task) {
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
                case ONLY_RUN -> scheduler.runTask(plugin, runnable);
                case DELAY_RUN -> // noinspection DataFlowIssue
                        scheduler.runTaskLater(plugin, runnable, delay);
                case TASK_RUN -> // noinspection DataFlowIssue
                        scheduler.runTaskTimer(plugin, runnable, delay, period);
            });
        } else {
            io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler) this.schedulerHandle;
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, (o) -> runnable.run());
                case DELAY_RUN -> // noinspection DataFlowIssue
                        scheduler.runDelayed(plugin, (o) -> runnable.run(), delay);
                case TASK_RUN -> // noinspection DataFlowIssue
                        scheduler.runAtFixedRate(plugin, (o) -> runnable.run(), delay, period);
            });
        }

        return ccTask;
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!this.ccScheduler.isFolia()) {
            BukkitScheduler scheduler = (BukkitScheduler) this.schedulerHandle;
            scheduler.cancelTasks(plugin);
        } else {
            io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler) this.schedulerHandle;
            scheduler.cancelTasks(plugin);
        }
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull Runnable runnable) {
        return this.handle(plugin, null, null, runnable);
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, null, null, consumer);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, long delay, @NotNull Runnable runnable) {
        return this.handle(plugin, delay, null, runnable);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, long delay, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, delay, null, consumer);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Runnable runnable, long delay) {
        return this.runTaskLater(plugin, delay, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Consumer<CCTask> consumer, long delay) {
        return this.runTaskLater(plugin, delay, consumer);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, long delay, long period, @NotNull Runnable runnable) {
        return this.handle(plugin, delay, period, runnable);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, long delay, long period, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, delay, period, consumer);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, delay, period, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Consumer<CCTask> consumer, long delay, long period) {
        return this.runTaskTimer(plugin, delay, period, consumer);
    }
}
