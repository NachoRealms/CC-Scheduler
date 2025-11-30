package cn.chengzhimeow.ccscheduler.runnable;

import cn.chengzhimeow.ccscheduler.task.Task;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public interface Runnable extends Task {
    void run();

    void runTask(JavaPlugin plugin);

    void runTaskLater(JavaPlugin plugin, long delay);

    void runTaskTimer(JavaPlugin plugin, long delay, long period);

    void runTaskAsynchronously(JavaPlugin plugin);

    void runTaskLaterAsynchronously(JavaPlugin plugin, long delay);

    void runTaskTimerAsynchronously(JavaPlugin plugin, long delay, long period);
}
