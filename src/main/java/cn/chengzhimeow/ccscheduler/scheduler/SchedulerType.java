package cn.chengzhimeow.ccscheduler.scheduler;

public enum SchedulerType {
    ONLY_RUN,
    DELAY_RUN,
    TASK_RUN;

    public static SchedulerType formDelayAndPeriod(Long delay, Long period) {
        if (delay != null && period == null) return SchedulerType.DELAY_RUN;
        else if (period != null) return SchedulerType.TASK_RUN;
        else return SchedulerType.ONLY_RUN;
    }
}
