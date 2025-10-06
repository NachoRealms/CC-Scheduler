package cn.chengzhimeow.ccscheduler.scheduler;

public enum SchedulerType {
    ONLY_RUN,
    DELAY_RUN,
    TASK_RUN;

    public static SchedulerType formDelayAndPeriod(long delay, long period) {
        if (delay != 0 && period == -1) {
            return SchedulerType.DELAY_RUN;
        } else if (delay != -1 && period != -1) {
            return SchedulerType.TASK_RUN;
        } else return SchedulerType.ONLY_RUN;
    }
}
