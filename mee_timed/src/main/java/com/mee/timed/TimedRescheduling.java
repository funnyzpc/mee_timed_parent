package com.mee.timed;

import java.util.concurrent.ScheduledFuture;

/**
 * TimedReschedulingApi
 *
 * @author shaoow
 * @version 1.0
 * @className TimedReschedulingApi
 * @date 2024/6/14 14:59
 */
public interface TimedRescheduling extends Runnable {
    ScheduledFuture<?> schedule();
}
