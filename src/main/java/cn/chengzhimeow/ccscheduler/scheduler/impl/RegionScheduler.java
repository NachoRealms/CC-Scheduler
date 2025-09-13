package cn.chengzhimeow.ccscheduler.scheduler.impl;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.SchedulerType;
import cn.chengzhimeow.ccscheduler.task.CCTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class RegionScheduler {
    private final CCScheduler ccScheduler;
    private final Object schedulerHandle;

    public RegionScheduler(CCScheduler ccScheduler) {
        this.ccScheduler = ccScheduler;
        if (this.ccScheduler.isFolia()) this.schedulerHandle = Bukkit.getRegionScheduler();
        else this.schedulerHandle = Bukkit.getScheduler();
    }

    @SuppressWarnings("unchecked")
    public CCTask handle(JavaPlugin plugin, World world, int cx, int cz, Object task, long delay, long period) {
        CCTask ccTask = new CCTask();

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
            io.papermc.paper.threadedregions.scheduler.RegionScheduler scheduler = (io.papermc.paper.threadedregions.scheduler.RegionScheduler) this.schedulerHandle;
            ccTask.setTaskHandle(switch (schedulerType) {
                case ONLY_RUN -> scheduler.run(plugin, world, cx, cz, (o) -> runnable.run());
                case DELAY_RUN -> scheduler.runDelayed(plugin, world, cx, cz, (o) -> runnable.run(), delay);
                case TASK_RUN -> scheduler.runAtFixedRate(plugin, world, cx, cz, (o) -> runnable.run(), delay, period);
            });
        }

        return ccTask;
    }

    public CCTask runTask(JavaPlugin plugin, World world, int cx, int cz, Runnable runnable) {
        return this.handle(plugin, world, cx, cz, runnable, 0L, 0L);
    }

    public CCTask runTask(JavaPlugin plugin, World world, int cx, int cz, Consumer<CCTask> consumer) {
        return this.handle(plugin, world, cx, cz, consumer, 0L, 0L);
    }

    public CCTask runTask(JavaPlugin plugin, Location location, Runnable runnable) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, runnable, 0L, 0L);
    }

    public CCTask runTask(JavaPlugin plugin, Location location, Consumer<CCTask> consumer) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, consumer, 0L, 0L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, World world, int cx, int cz, Runnable runnable, long delay) {
        return this.handle(plugin, world, cx, cz, runnable, delay, 0L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, World world, int cx, int cz, Consumer<CCTask> consumer, long delay) {
        return this.handle(plugin, world, cx, cz, consumer, delay, 0L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, Location location, Runnable runnable, long delay) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, runnable, delay, 0L);
    }

    public CCTask runTaskLater(JavaPlugin plugin, Location location, Consumer<CCTask> consumer, long delay) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, consumer, delay, 0L);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, World world, int cx, int cz, Runnable runnable, long delay, long period) {
        return this.handle(plugin, world, cx, cz, runnable, delay, period);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, World world, int cx, int cz, Consumer<CCTask> consumer, long delay, long period) {
        return this.handle(plugin, world, cx, cz, consumer, delay, period);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, Location location, Runnable runnable, long delay, long period) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, runnable, delay, period);
    }

    public CCTask runTaskTimer(JavaPlugin plugin, Location location, Consumer<CCTask> consumer, long delay, long period) {
        return this.handle(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, consumer, delay, period);
    }
}
