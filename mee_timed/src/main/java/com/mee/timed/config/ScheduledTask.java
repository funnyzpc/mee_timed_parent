
package com.mee.timed.config;

import org.springframework.lang.Nullable;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;

import java.util.concurrent.ScheduledFuture;

/**
 * A representation of a scheduled task at runtime,
 * used as a return value for scheduling methods.
 *
 * @author Juergen Hoeller
 * @since 4.3
 * @see ScheduledTaskRegistrar#scheduleCronTask(CronTask)
 * @see ScheduledTaskRegistrar#scheduleFixedRateTask(FixedRateTask)
 * @see ScheduledTaskRegistrar#scheduleFixedDelayTask(FixedDelayTask)
 * @see ScheduledFuture
 */
public final class ScheduledTask {

	private final Task task;

	@Nullable
	public volatile ScheduledFuture<?> future;


	public ScheduledTask(Task task) {
		this.task = task;
	}


	/**
	 * Return the underlying task (typically a {@link CronTask},
	 * {@link FixedRateTask} or {@link FixedDelayTask}).
	 * @since 5.0.2
	 */
	public Task getTask() {
		return this.task;
	}

	/**
	 * Trigger cancellation of this scheduled task.
	 * <p>This variant will force interruption of the task if still running.
	 * @see #cancel(boolean)
	 */
	public void cancel() {
		cancel(true);
	}

	/**
	 * Trigger cancellation of this scheduled task.
	 * @param mayInterruptIfRunning whether to force interruption of the task
	 * if still running (specify {@code false} to allow the task to complete)
	 * @since 5.3.18
	 * @see ScheduledFuture#cancel(boolean)
	 */
	public void cancel(boolean mayInterruptIfRunning) {
		ScheduledFuture<?> future = this.future;
		if (future != null) {
			future.cancel(mayInterruptIfRunning);
		}
	}

	@Override
	public String toString() {
		return this.task.toString();
	}

}
