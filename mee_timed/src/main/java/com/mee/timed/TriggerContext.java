package com.mee.timed;

import org.springframework.lang.Nullable;
import org.springframework.scheduling.TaskScheduler;

import java.time.Clock;
import java.util.Date;

/**
 * TriggerContext
 *
 * @author shaoow
 * @version 1.0
 * @className TriggerContext
 * @date 2024/7/10 10:39
 */
public interface TriggerContext {
    /**
     * Return the clock to use for trigger calculation.
     * @since 5.3
     * @see TaskScheduler#getClock()
     * @see Clock#systemDefaultZone()
     */
    default Clock getClock() {
        return Clock.systemDefaultZone();
    }

    /**
     * Return the last <i>scheduled</i> execution time of the task,
     * or {@code null} if not scheduled before.
     */
    @Nullable
    Date lastScheduledExecutionTime();

    /**
     * Return the last <i>actual</i> execution time of the task,
     * or {@code null} if not scheduled before.
     */
    @Nullable
    Date lastActualExecutionTime();

    /**
     * Return the last completion time of the task,
     * or {@code null} if not scheduled before.
     */
    @Nullable
    Date lastCompletionTime();

}
