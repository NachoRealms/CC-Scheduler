package cn.chengzhimeow.ccscheduler.scheduler;

import cn.chengzhimeow.ccscheduler.scheduler.impl.AsyncScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.impl.EntityScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.impl.GlobalRegionScheduler;
import cn.chengzhimeow.ccscheduler.scheduler.impl.RegionScheduler;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class CCScheduler {
    @Getter
    private static CCScheduler instance;

    private final boolean folia;

    private final AsyncScheduler asyncScheduler;
    private final EntityScheduler entityScheduler;
    private final GlobalRegionScheduler globalRegionScheduler;
    private final RegionScheduler regionScheduler;

    private CCScheduler() {
        boolean folia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException ignored) {
        }

        this.folia = folia;
        this.asyncScheduler = new AsyncScheduler(this);
        this.entityScheduler = new EntityScheduler(this);
        this.globalRegionScheduler = new GlobalRegionScheduler(this);
        this.regionScheduler = new RegionScheduler(this);
    }

    public void cancel(@NotNull Plugin plugin) {
        this.getGlobalRegionScheduler().cancel(plugin);
        this.getAsyncScheduler().cancel(plugin);
    }
}