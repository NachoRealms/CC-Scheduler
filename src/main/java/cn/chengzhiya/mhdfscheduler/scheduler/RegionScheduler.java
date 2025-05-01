package cn.chengzhiya.mhdfscheduler.scheduler;

import cn.chengzhiya.mhdfscheduler.task.MHDFTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

public final class RegionScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.RegionScheduler regionScheduler;

    public RegionScheduler() {
        if (MHDFScheduler.isFolia()) {
            regionScheduler = Bukkit.getRegionScheduler();
        } else {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public MHDFTask runTask(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Runnable task) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTask(plugin, task));
        }

        return new MHDFTask(regionScheduler.run(plugin, world, chunkX, chunkZ, (o) -> task.run()));
    }

    public MHDFTask runTask(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task) {
        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTask(plugin, task));
        }

        return new MHDFTask(regionScheduler.run(plugin, location, (o) -> task.run()));
    }

    public MHDFTask runTaskLater(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Runnable task, long delayTicks) {
        if (delayTicks < 1) {
            delayTicks = 1;
        }

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskLater(plugin, task, delayTicks));
        }

        return new MHDFTask(regionScheduler.runDelayed(plugin, world, chunkX, chunkZ, (o) -> task.run(), delayTicks));
    }

    public MHDFTask runTaskLater(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task, long delayTicks) {
        if (delayTicks < 1) {
            delayTicks = 1;
        }

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskLater(plugin, task, delayTicks));
        }

        return new MHDFTask(regionScheduler.runDelayed(plugin, location, (o) -> task.run(), delayTicks));
    }

    public MHDFTask runTaskTimer(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) {
            initialDelayTicks = 1;
        }
        if (periodTicks < 1) {
            periodTicks = 1;
        }

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskTimer(plugin, task, initialDelayTicks, periodTicks));
        }

        return new MHDFTask(regionScheduler.runAtFixedRate(plugin, world, chunkX, chunkZ, (o) -> task.run(), initialDelayTicks, periodTicks));
    }

    public MHDFTask runTaskTimer(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) {
            initialDelayTicks = 1;
        }
        if (periodTicks < 1) {
            periodTicks = 1;
        }

        if (!MHDFScheduler.isFolia()) {
            return new MHDFTask(bukkitScheduler.runTaskTimer(plugin, task, initialDelayTicks, periodTicks));
        }

        return new MHDFTask(regionScheduler.runAtFixedRate(plugin, location, (o) -> task.run(), initialDelayTicks, periodTicks));
    }
}