
package com.mee.timed.annotation;

import com.mee.timed.config.TimedTaskRegistrar;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Annotation that marks a method to be scheduled. Exactly one of the
 * {@link #cron}, {@link #fixedDelay}, or {@link #fixedRate} attributes must be
 * specified.
 *
 * <p>The annotated method must expect no arguments. It will typically have
 * a {@code void} return type; if not, the returned value will be ignored
 * when called through the scheduler.
 *
 * <p>Processing of {@code @Scheduled} annotations is performed by
 * registering a {@link ScheduledAnnotationBeanPostProcessor}. This can be
 * done manually or, more conveniently, through the {@code <task:annotation-driven/>}
 * XML element or {@link EnableScheduling @EnableScheduling} annotation.
 *
 * <p>This annotation may be used as a <em>meta-annotation</em> to create custom
 * <em>composed annotations</em> with attribute overrides.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Dave Syer
 * @author Chris Beams
 * @author Victor Brown
 * @author Sam Brannen
 * @since 3.0
 * @see EnableScheduling
 * @see ScheduledAnnotationBeanPostProcessor
 * @see Schedules
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(MeeTimeds.class)
public @interface MeeTimed {

	/**
	 * A special cron expression value that indicates a disabled trigger: {@value}.
	 * <p>This is primarily meant for use with <code>${...}</code> placeholders,
	 * allowing for external disabling of corresponding scheduled methods.
	 * @since 5.1
	 * @see ScheduledTaskRegistrar#CRON_DISABLED
	 */
	String CRON_DISABLED = TimedTaskRegistrar.CRON_DISABLED;


	/**
	 * A cron-like expression, extending the usual UN*X definition to include triggers
	 * on the second, minute, hour, day of month, month, and day of week.
	 * <p>For example, {@code "0 * * * * MON-FRI"} means once per minute on weekdays
	 * (at the top of the minute - the 0th second).
	 * <p>The fields read from left to right are interpreted as follows.
	 * <ul>
	 * <li>second</li>
	 * <li>minute</li>
	 * <li>hour</li>
	 * <li>day of month</li>
	 * <li>month</li>
	 * <li>day of week</li>
	 * </ul>
	 * <p>The special value {@link #CRON_DISABLED "-"} indicates a disabled cron
	 * trigger, primarily meant for externally specified values resolved by a
	 * <code>${...}</code> placeholder.
	 * @return an expression that can be parsed to a cron schedule
	 * @see org.springframework.scheduling.support.CronExpression#parse(String)
	 */
	String cron() default "";

	/**
	 * A time zone for which the cron expression will be resolved. By default, this
	 * attribute is the empty String (i.e. the server's local time zone will be used).
	 * @return a zone id accepted by {@link java.util.TimeZone#getTimeZone(String)},
	 * or an empty String to indicate the server's default time zone
	 * @since 4.0
	 * @see org.springframework.scheduling.support.CronTrigger#CronTrigger(String, java.util.TimeZone)
	 * @see java.util.TimeZone
	 */
	String zone() default "";

	/**
	 * Execute the annotated method with a fixed period between the end of the
	 * last invocation and the start of the next.
	 * <p>The time unit is milliseconds by default but can be overridden via
	 * {@link #timeUnit}.
	 * @return the delay
	 */
	long fixedDelay() default -1;

//	/**
//	 * Execute the annotated method with a fixed period between the end of the
//	 * last invocation and the start of the next.
//	 * <p>The time unit is milliseconds by default but can be overridden via
//	 * {@link #timeUnit}.
//	 * @return the delay as a String value &mdash; for example, a placeholder
//	 * or a {@link java.time.Duration#parse java.time.Duration} compliant value
//	 * @since 3.2.2
//	 */
//	String fixedDelayString() default "";

	/**
	 * Execute the annotated method with a fixed period between invocations.
	 * <p>The time unit is milliseconds by default but can be overridden via
	 * {@link #timeUnit}.
	 * @return the period
	 */
	long fixedRate() default -1;

//	/**
//	 * Execute the annotated method with a fixed period between invocations.
//	 * <p>The time unit is milliseconds by default but can be overridden via
//	 * {@link #timeUnit}.
//	 * @return the period as a String value &mdash; for example, a placeholder
//	 * or a {@link java.time.Duration#parse java.time.Duration} compliant value
//	 * @since 3.2.2
//	 */
//	String fixedRateString() default "";

	/**
	 * Number of units of time to delay before the first execution of a
	 * {@link #fixedRate} or {@link #fixedDelay} task.
	 * <p>The time unit is milliseconds by default but can be overridden via
	 * {@link #timeUnit}.
	 * @return the initial
	 * @since 3.2
	 * 第一次执行延迟数
	 */
	long initialDelay() default -1;

	/**
	 * Number of units of time to delay before the first execution of a
	 * {@link #fixedRate} or {@link #fixedDelay} task.
	 * <p>The time unit is milliseconds by default but can be overridden via
	 * {@link #timeUnit}.
	 * @return the initial delay as a String value &mdash; for example, a placeholder
	 * or a {@link java.time.Duration#parse java.time.Duration} compliant value
	 * @since 3.2.2
	 */
	String initialDelayString() default "";

	/**
	 * The {@link TimeUnit} to use for {@link #fixedDelay}, {@link #fixedDelayString},
	 * {@link #fixedRate}, {@link #fixedRateString}, {@link #initialDelay}, and
	 * {@link #initialDelayString}.
	 * <p>Defaults to {@link TimeUnit#MILLISECONDS}.
	 * <p>This attribute is ignored for {@linkplain #cron() cron expressions}
	 * and for {@link java.time.Duration} values supplied via {@link #fixedDelayString},
	 * {@link #fixedRateString}, or {@link #initialDelayString}.
	 * @return the {@code TimeUnit} to use
	 * @since 5.3.10
	 */
	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

	/** ========================================================= LOCK ==================================================== **/
	/**
	 * Lock name.
	 */
	String lockName()  default "";

	/**
	 * How long the lock should be kept in case the machine which obtained the lock died before releasing it.
	 * This is just a fallback, under normal circumstances the lock is released as soon the tasks finishes. Can be any format
	 * supported by <a href="https://docs.micronaut.io/latest/guide/config.html#_duration_conversion">Duration Conversion</a>
	 * <p>
	 */
	String lockAtMostFor() default "";

	/**
	 * The lock will be held at least for this period of time. Can be used if you really need to execute the task
	 * at most once in given period of time. If the duration of the task is shorter than clock difference between nodes, the task can
	 * be theoretically executed more than once (one node after another). By setting this parameter, you can make sure that the
	 * lock will be kept at least for given period of time. Can be any format
	 * supported by <a href="https://docs.micronaut.io/latest/guide/config.html#_duration_conversion">Duration Conversion</a>
	 */
	String lockAtLeastFor() default "";

}
