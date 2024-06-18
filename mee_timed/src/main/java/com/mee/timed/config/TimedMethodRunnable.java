package com.mee.timed.config;

import com.mee.timed.annotation.MeeTimed;
import com.mee.timed.data.JobEntity;
import com.mee.timed.data.JobExecutionContextImpl;
import com.mee.timed.template.ClockProvider;
import com.mee.timed.template.LockConfiguration;
import com.mee.timed.template.LockProvider;
import com.mee.timed.template.SimpleLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.MethodInvokingRunnable;
import org.springframework.util.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

/**
 * Variant of {@link MethodInvokingRunnable} meant to be used for processing
 * of no-arg scheduled methods. Propagates user exceptions to the caller,
 * assuming that an error strategy for Runnables is in place.
 *
 * ScheduledMethodRunnable
 * @author Juergen Hoeller
 * @since 3.0.6
 * @see org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor
 */
public class TimedMethodRunnable implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimedMethodRunnable.class);
	private final Object target;

	private final Method method;
	private final MeeTimed scheduled;
	private final LockProvider lockProvider;
	private WeakReference<LockConfiguration> lockConfiguration;//= new WeakReference<LockConfiguration>();
//	private final Duration defaultLockAtMostFor = Duration.parse("PT1M");
//	private final Duration defaultLockAtLeastFor = Duration.parse("PT1S");

//	/**
//	 * Create a {@code ScheduledMethodRunnable} for the given target instance,
//	 * calling the specified method.
//	 * @param target the target instance to call the method on
//	 * @param method the target method to call
//	 */
//	@Deprecated
//	public TimedMethodRunnable(Object target, Method method) {
//		this.target = target;
//		this.method = method;
////		this.jdbcTemplate = null;
//		this.lockProvider = null;
//	}
//
//	/**
//	 * Create a {@code ScheduledMethodRunnable} for the given target instance,
//	 * calling the specified method by name.
//	 * @param target the target instance to call the method on
//	 * @param methodName the name of the target method
//	 * @throws NoSuchMethodException if the specified method does not exist
//	 */
//	@Deprecated
//	public TimedMethodRunnable(Object target, String methodName) throws NoSuchMethodException {
//		this.target = target;
//		this.method = target.getClass().getMethod(methodName);
////		this.jdbcTemplate = null;
//		this.lockProvider = null;
//	}

	public TimedMethodRunnable(Object target, Method method,MeeTimed scheduled,LockProvider lockProvider) {
		this.target = target;
		this.method = method;
		this.scheduled = scheduled;
//		this.jdbcTemplate = jdbcTemplate;
		this.lockProvider = lockProvider;
	}

//	/**
//	 * Return the target instance to call the method on.
//	 */
//	public Object getTarget() {
//		return this.target;
//	}
//
//	/**
//	 * Return the target method to call.
//	 */
//	public Method getMethod() {
//		return this.method;
//	}


	@Override
	public void run() {
		try {
			Method mth = this.method;
			// 获取注解
			MeeTimed annotation = this.scheduled;
			// 开始加锁
			LockConfiguration lockConfiguration;
			if(this.lockConfiguration !=null && (lockConfiguration= this.lockConfiguration.get())!=null){
				lockConfiguration.setCreatedAt(ClockProvider.now());
				LOGGER.debug("获取到缓存key:{}",lockConfiguration.getName());
			}else{
				this.lockConfiguration = new WeakReference<>(lockConfiguration=this.getLockConfiguration(annotation,mth));
				LOGGER.error("写入到缓存key:{}",lockConfiguration.getName());
			}
			// 获取锁
			Optional<SimpleLock> lock = lockProvider.lock(lockConfiguration);
//			if(lock.isEmpty()){
			if(!lock.isPresent()){
				LOGGER.error("已加锁:{}",lockConfiguration.getName());
				return;
			}
			// 去掉 private 等不可见修饰
			ReflectionUtils.makeAccessible(mth);
			// 执行目标函数
			if(mth.getParameterCount()>0){
				JobEntity jobRecord = lockProvider.getStorageAccessor().findJobRecord(lockConfiguration);
				mth.invoke(this.target,this.buildParam(jobRecord));
			}else{
				mth.invoke(this.target);
			}
			// 释放锁
			lock.get().unlock();
//			lockProvider.getStorageAccessor().unlock(lockConfiguration);
		}
		catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
		}
		catch (IllegalAccessException ex) {
			throw new UndeclaredThrowableException(ex);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private LockConfiguration getLockConfiguration(MeeTimed annotation,Method mth) throws IllegalArgumentException {
//		// 如果没有定义lockName 则 key=className#methodName 否则 key=className#methodName#lockName
//		final String lockName = annotation.lockName();
//		if(null!=lockName && lockName.contains("#")){
//			throw new IllegalAccessException("@MeeTimed[lockName] contains '#");
//		}
//		final String key = mth.getDeclaringClass().getName()+"#"+mth.getName()+("".equals(lockName)?"":"#"+lockName);
		final String key = buildKey(annotation,mth);
		final String lockAtMostFor = annotation.lockAtMostFor();
		final String lockAtLeastFor = annotation.lockAtLeastFor();
		return new LockConfiguration(
				ClockProvider.now(),
				key,
				null==lockAtMostFor||"".equals(lockAtMostFor.trim())? lockProvider.getConfiguration().getDefaultMostFor():Duration.parse(lockAtMostFor),
				null==lockAtLeastFor||"".equals(lockAtLeastFor.trim())?lockProvider.getConfiguration().getDefaultLeastFor():Duration.parse(lockAtLeastFor)
		);
	}

	public static String buildKey(MeeTimed annotation,Method mth) throws IllegalArgumentException {
		final String lockName = annotation.lockName();
		if(null!=lockName && lockName.contains("#")) {
			throw new IllegalArgumentException("@MeeTimed[lockName] not allowed contains '#");
		}
		return mth.getDeclaringClass().getName()+"#"+mth.getName()+("".equals(lockName)?"":"#"+lockName);
	}

	private JobExecutionContextImpl buildParam(JobEntity jobEntity){
		return new JobExecutionContextImpl(new Date())
				.setJobDataJson(jobEntity.getData())
				.setJobInfo(jobEntity);
	}
	@Override
	public String toString() {
		return this.method.getDeclaringClass().getName() + "." + this.method.getName();
	}

}
