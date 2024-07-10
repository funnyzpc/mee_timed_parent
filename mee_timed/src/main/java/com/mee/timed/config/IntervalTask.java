package com.mee.timed.config;

/**
 * IntervalTask
 *
 * @author shaoow
 * @version 1.0
 * @className IntervalTask
 * @date 2024/7/10 11:03
 */
public class IntervalTask extends Task{
    private final long interval;

    private final long initialDelay;


    /**
     * Create a new {@code IntervalTask}.
     * @param runnable the underlying task to execute
     * @param interval how often in milliseconds the task should be executed
     * @param initialDelay the initial delay before first execution of the task
     */
    public IntervalTask(Runnable runnable, long interval, long initialDelay) {
        super(runnable);
        this.interval = interval;
        this.initialDelay = initialDelay;
    }

//    /**
//     * Create a new {@code IntervalTask} with no initial delay.
//     * @param runnable the underlying task to execute
//     * @param interval how often in milliseconds the task should be executed
//     */
//    public IntervalTask(Runnable runnable, long interval) {
//        this(runnable, interval, 0);
//    }


    /**
     * Return how often in milliseconds the task should be executed.
     */
    public long getInterval() {
        return this.interval;
    }

    /**
     * Return the initial delay before first execution of the task.
     */
    public long getInitialDelay() {
        return this.initialDelay;
    }

}
