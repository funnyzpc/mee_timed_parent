package com.mee.timed;

import org.springframework.lang.Nullable;

import java.util.Date;

/**
 * Trigger
 *
 * @author shaoow
 * @version 1.0
 * @className Trigger
 * @date 2024/7/10 10:38
 */
public interface Trigger {

    /**
     * Determine the next execution time according to the given trigger context.
     * @param triggerContext context object encapsulating last execution times
     * and last completion time
     * @return the next execution time as defined by the trigger,
     * or {@code null} if the trigger won't fire anymore
     */
    @Nullable
    Date nextExecutionTime(TriggerContext triggerContext);

}
