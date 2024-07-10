package com.mee.timed.config;

/**
 * FixedRateTask
 *
 * @author shaoow
 * @version 1.0
 * @className FixedRateTask
 * @date 2024/7/10 11:03
 */
public class FixedRateTask extends IntervalTask {

    public FixedRateTask(Runnable runnable, long interval, long initialDelay) {
        super(runnable, interval, initialDelay);
    }
}
