package com.mee.timed.config;

import org.springframework.util.Assert;

/**
 * Task
 *
 * @author shaoow
 * @version 1.0
 * @className Task
 * @date 2024/7/10 10:57
 */
public class Task {
    private final Runnable runnable;


    /**
     * Create a new {@code Task}.
     * @param runnable the underlying task to execute
     */
    public Task(Runnable runnable) {
        Assert.notNull(runnable, "Runnable must not be null");
        this.runnable = runnable;
    }


    /**
     * Return the underlying task.
     */
    public Runnable getRunnable() {
        return this.runnable;
    }


    @Override
    public String toString() {
        return this.runnable.toString();
    }
}
