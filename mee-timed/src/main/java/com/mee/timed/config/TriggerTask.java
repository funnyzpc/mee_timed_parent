package com.mee.timed.config;

import com.mee.timed.Trigger;
import org.springframework.util.Assert;

/**
 * TriggerTask
 *
 * @author shaoow
 * @version 1.0
 * @className TriggerTask
 * @date 2024/7/10 10:44
 */
public class TriggerTask extends Task {
    private final Trigger trigger;


    /**
     * Create a new {@link org.springframework.scheduling.config.TriggerTask}.
     * @param runnable the underlying task to execute
     * @param trigger specifies when the task should be executed
     */
    public TriggerTask(Runnable runnable, Trigger trigger) {
        super(runnable);
        Assert.notNull(trigger, "Trigger must not be null");
        this.trigger = trigger;
    }


    /**
     * Return the associated trigger.
     */
    public Trigger getTrigger() {
        return this.trigger;
    }

}
