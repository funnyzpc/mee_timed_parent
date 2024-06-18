package com.mee.timed.config;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.SchedulingAwareRunnable;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Adapter that takes a {@code java.util.concurrent.Executor} and exposes
 * a Spring {@link org.springframework.core.task.TaskExecutor} for it.
 * Also detects an extended {@code java.util.concurrent.ExecutorService}, adapting
 * the {@link org.springframework.core.task.AsyncTaskExecutor} interface accordingly.
 *
 * <p>Autodetects a JSR-236 {@link javax.enterprise.concurrent.ManagedExecutorService}
 * in order to expose {@link javax.enterprise.concurrent.ManagedTask} adapters for it,
 * exposing a long-running hint based on {@link SchedulingAwareRunnable} and an identity
 * name based on the given Runnable/Callable's {@code toString()}. For JSR-236 style
 * lookup in a Java EE 7 environment, consider using {@link DefaultManagedTaskExecutor}.
 *
 * <p>Note that there is a pre-built {@link ThreadPoolTaskExecutor} that allows
 * for defining a {@link java.util.concurrent.ThreadPoolExecutor} in bean style,
 * exposing it as a Spring {@link org.springframework.core.task.TaskExecutor} directly.
 * This is a convenient alternative to a raw ThreadPoolExecutor definition with
 * a separate definition of the present adapter class.
 *
 *  ConcurrentTaskExecutor
 * @author Juergen Hoeller
 * @since 2.0
 * @see Executor
 * @see java.util.concurrent.ExecutorService
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see Executors
 * @see DefaultManagedTaskExecutor
 * @see ThreadPoolTaskExecutor
 */
public class ConcurrentTimedExecutor implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {

	private Executor concurrentExecutor;

	private TaskExecutorAdapter adaptedExecutor;


//	/**
//	 * Create a new ConcurrentTaskExecutor, using a single thread executor as default.
//	 * @see Executors#newSingleThreadExecutor()
//	 */
//	public ConcurrentTimedExecutor() {
//		this.concurrentExecutor = Executors.newSingleThreadExecutor();
//		this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
//	}

	/**
	 * Create a new ConcurrentTaskExecutor, using the given {@link Executor}.
	 * <p>Autodetects a JSR-236 {@link javax.enterprise.concurrent.ManagedExecutorService}
	 * in order to expose {@link javax.enterprise.concurrent.ManagedTask} adapters for it.
	 * @param executor the {@link Executor} to delegate to
	 */
	public ConcurrentTimedExecutor(@Nullable Executor executor) {
		if( null==executor ){
			throw new IllegalArgumentException("executor is empty!");
		}
//		this.concurrentExecutor = (executor != null ? executor : Executors.newSingleThreadExecutor());
		this.concurrentExecutor = executor;
		this.adaptedExecutor = getAdaptedExecutor(this.concurrentExecutor);
	}


	/**
	 * Specify the {@link Executor} to delegate to.
	 * <p>Autodetects a JSR-236 {@link javax.enterprise.concurrent.ManagedExecutorService}
	 * in order to expose {@link javax.enterprise.concurrent.ManagedTask} adapters for it.
	 */
	public final void setConcurrentExecutor(@Nullable Executor executor) {
		if( null==executor ){
			throw new IllegalArgumentException("executor is empty!");
		}
//		this.concurrentExecutor = (executor != null ? executor : Executors.newSingleThreadExecutor());
		this.concurrentExecutor =  executor;
		this.adaptedExecutor = getAdaptedExecutor(this.concurrentExecutor);
	}

	/**
	 * Return the {@link Executor} that this adapter delegates to.
	 */
	public final Executor getConcurrentExecutor() {
		return this.concurrentExecutor;
	}

	/**
	 * Specify a custom {@link TaskDecorator} to be applied to any {@link Runnable}
	 * about to be executed.
	 * <p>Note that such a decorator is not necessarily being applied to the
	 * user-supplied {@code Runnable}/{@code Callable} but rather to the actual
	 * execution callback (which may be a wrapper around the user-supplied task).
	 * <p>The primary use case is to set some execution context around the task's
	 * invocation, or to provide some monitoring/statistics for task execution.
	 * <p><b>NOTE:</b> Exception handling in {@code TaskDecorator} implementations
	 * is limited to plain {@code Runnable} execution via {@code execute} calls.
	 * In case of {@code #submit} calls, the exposed {@code Runnable} will be a
	 * {@code FutureTask} which does not propagate any exceptions; you might
	 * have to cast it and call {@code Future#get} to evaluate exceptions.
	 * @since 4.3
	 */
	public final void setTaskDecorator(TaskDecorator taskDecorator) {
		this.adaptedExecutor.setTaskDecorator(taskDecorator);
	}


	@Override
	public void execute(Runnable task) {
		this.adaptedExecutor.execute(task);
	}

	@Deprecated
	@Override
	public void execute(Runnable task, long startTimeout) {
		this.adaptedExecutor.execute(task, startTimeout);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return this.adaptedExecutor.submit(task);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return this.adaptedExecutor.submit(task);
	}

	@Override
	public ListenableFuture<?> submitListenable(Runnable task) {
		return this.adaptedExecutor.submitListenable(task);
	}

	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return this.adaptedExecutor.submitListenable(task);
	}


	private static TaskExecutorAdapter getAdaptedExecutor(Executor concurrentExecutor) {
//		if (managedExecutorServiceClass != null && managedExecutorServiceClass.isInstance(concurrentExecutor)) {
//			return new ManagedTaskExecutorAdapter(concurrentExecutor);
//		}
		return new TaskExecutorAdapter(concurrentExecutor);
	}


}
