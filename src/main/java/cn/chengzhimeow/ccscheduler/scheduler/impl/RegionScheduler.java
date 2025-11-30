package cn.chengzhimeow.ccscheduler.scheduler.impl;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.SchedulerType;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class RegionScheduler {
    private final CCScheduler ccScheduler;
    private final Object schedulerHandle;

    public RegionScheduler(@NotNull CCScheduler ccScheduler) {
        this.ccScheduler = ccScheduler;
        if (this.ccScheduler.isFolia()) this.schedulerHandle = Bukkit.getRegionScheduler();
        else this.schedulerHandle = Bukkit.getScheduler();
    }

    public @NotNull CCTask handle(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, @Nullable Long delay, @Nullable Long period, @NotNull Object task) {
        CCTask ccTask = new CCTask(this.ccScheduler);

        Runnable runnable;
        if (task instanceof Runnable r) runnable = r;
        else if (task instanceof Consumer<?> consumer)
            // noinspection unchecked
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
            io.papermc.paper.threadedregions.scheduler.RegionScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.RegionScheduler) this.schedulerHandle;
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, world, cx, cz, (o) -> runnable.run());
                case DELAY_RUN -> // noinspection DataFlowIssue
                        scheduler.runDelayed(plugin, world, cx, cz, (o) -> runnable.run(), delay);
                case TASK_RUN -> // noinspection DataFlowIssue
                        scheduler.runAtFixedRate(plugin, world, cx, cz, (o) -> runnable.run(), delay, period);
            });
        }

        return ccTask;
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, @NotNull Runnable runnable) {
        return this.handle(plugin, world, cx, cz, null, null, runnable);
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, world, cx, cz, null, null, consumer);
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable runnable) {
        return this.runTask(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, runnable);
    }

    public @NotNull CCTask runTask(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Consumer<CCTask> consumer) {
        return this.runTask(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, consumer);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, long delay, @NotNull Runnable runnable) {
        return this.handle(plugin, world, cx, cz, delay, null, runnable);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, long delay, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, world, cx, cz, delay, null, consumer);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Location location, long delay, @NotNull Runnable runnable) {
        return this.runTaskLater(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, delay, runnable);
    }

    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Location location, long delay, @NotNull Consumer<CCTask> consumer) {
        return this.runTaskLater(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, delay, consumer);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, @NotNull Runnable runnable, long delay) {
        return this.runTaskLater(plugin, world, cx, cz, delay, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, @NotNull Consumer<CCTask> consumer, long delay) {
        return this.runTaskLater(plugin, world, cx, cz, delay, consumer);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable runnable, long delay) {
        return this.runTaskLater(plugin, location, delay, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskLater(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Consumer<CCTask> consumer, long delay) {
        return this.runTaskLater(plugin, location, delay, consumer);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, long delay, long period, @NotNull Runnable runnable) {
        return this.handle(plugin, world, cx, cz, delay, period, runnable);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull World world, int cx, int cz, long delay, long period, @NotNull Consumer<CCTask> consumer) {
        return this.handle(plugin, world, cx, cz, delay, period, consumer);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Location location, long delay, long period, @NotNull Runnable runnable) {
        return this.runTaskTimer(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, delay, period, runnable);
    }

    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, @NotNull Location location, long delay, long period, @NotNull Consumer<CCTask> consumer) {
        return this.runTaskTimer(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, delay, period, consumer);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, World world, int cx, int cz, @NotNull Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, world, cx, cz, delay, period, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, World world, int cx, int cz, @NotNull Consumer<CCTask> runnable, long delay, long period) {
        return this.runTaskTimer(plugin, world, cx, cz, delay, period, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, Location location, @NotNull Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, location, delay, period, runnable);
    }

    @Deprecated
    public @NotNull CCTask runTaskTimer(@NotNull JavaPlugin plugin, Location location, @NotNull Consumer<CCTask> consumer, long delay, long period) {
        return this.runTaskTimer(plugin, location, delay, period, consumer);
    }
}
