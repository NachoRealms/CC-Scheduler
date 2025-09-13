package cn.chengzhiya.mhdfscheduler.scheduler.impl;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdfscheduler.scheduler.SchedulerType;
import cn.chengzhiya.mhdfscheduler.task.MHDFTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class RegionScheduler {
    private final MHDFScheduler mhdfScheduler;
    private final Object schedulerHandle;

    public RegionScheduler(MHDFScheduler mhdfScheduler) {
        this.mhdfScheduler = mhdfScheduler;
        if (this.mhdfScheduler.isFolia()) this.schedulerHandle = Bukkit.getRegionScheduler();
        else this.schedulerHandle = Bukkit.getScheduler();
    }

    @SuppressWarnings("unchecked")
    public MHDFTask handle(JavaPlugin plugin, World world, int cx, int cz, Object task, long delay, long period) {
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
                case ONLY_RUN -> scheduler.runTaskAsynchronously(plugin, runnable);
                case DELAY_RUN -> scheduler.runTaskLaterAsynchronously(plugin, runnable, delay);
                case TASK_RUN -> scheduler.runTaskTimerAsynchronously(plugin, runnable, delay, period);
            });
        } else {
            io.papermc.paper.threadedregions.scheduler.RegionScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.RegionScheduler) this.schedulerHandle;
            mhdfTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, world, cx, cz, (o) -> runnable.run());
                case DELAY_RUN -> scheduler.runDelayed(plugin, world, cx, cz, (o) -> runnable.run(), delay);
                case TASK_RUN -> scheduler.runAtFixedRate(plugin, world, cx, cz, (o) -> runnable.run(), delay, period);
            });
        }

        return mhdfTask;
    }

    public MHDFTask runTask(JavaPlugin plugin, World world, int cx, int cz, Runnable runnable) {
        return this.handle(plugin, world, cx, cz, runnable, 0L, 0L);
    }

    public MHDFTask runTask(JavaPlugin plugin, World world, int cx, int cz, Consumer<MHDFTask> consumer) {
        return this.handle(plugin, world, cx, cz, consumer, 0L, 0L);
    }

    public MHDFTask runTask(JavaPlugin plugin, Location location, Runnable runnable) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, runnable, 0L, 0L);
    }

    public MHDFTask runTask(JavaPlugin plugin, Location location, Consumer<MHDFTask> consumer) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, consumer, 0L, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, World world, int cx, int cz, Runnable runnable, long delay) {
        return this.handle(plugin, world, cx, cz, runnable, delay, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, World world, int cx, int cz, Consumer<MHDFTask> consumer, long delay) {
        return this.handle(plugin, world, cx, cz, consumer, delay, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, Location location, Runnable runnable, long delay) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, runnable, delay, 0L);
    }

    public MHDFTask runTaskLater(JavaPlugin plugin, Location location, Consumer<MHDFTask> consumer, long delay) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, consumer, delay, 0L);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, World world, int cx, int cz, Runnable runnable, long delay, long period) {
        return this.handle(plugin, world, cx, cz, runnable, delay, period);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, World world, int cx, int cz, Consumer<MHDFTask> consumer, long delay, long period) {
        return this.handle(plugin, world, cx, cz, consumer, delay, period);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, Location location, Runnable runnable, long delay, long period) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, runnable, delay, period);
    }

    public MHDFTask runTaskTimer(JavaPlugin plugin, Location location, Consumer<MHDFTask> consumer, long delay, long period) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, consumer, delay, period);
    }
}
