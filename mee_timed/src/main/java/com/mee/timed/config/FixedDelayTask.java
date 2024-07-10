package com.mee.timed.config;

/**
 * FixedDelayTask
 *
 * @author shaoow
 * @version 1.0
 * @className FixedDelayTask
 * @date 2024/7/10 11:04
 */
public class FixedDelayTask extends IntervalTask{

    public FixedDelayTask(Runnable runnable, long interval, long initialDelay) {
        super(runnable, interval, initialDelay);
    }

}
