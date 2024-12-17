
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
	 * You need quartz CRON expression （您需要使用quartz的表达式，而不是spring提供的schedule的表达式）
	 *
	 * 使用quartz的cron表达式可以表达 诸如 <code>0 30 10 L * ?</code>(每月最后一天的十点半) <code>0 0/4 * ? * MON-FRI</code>(周一至周五内每四分钟) 这样的表达式
	 *
	 * @return an expression that can be parsed to a cron schedule
	 * @see org.springframework.scheduling.support.CronExpression#parse(String)
	 *
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
	 * 如果指定时区了则以指定时区的时间执行任务，默认是本地时区
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
	 *  锁定名称,一般用在@MeeTimeds定义的多执行时间中，本质上是为了区分任务锁；多时间下若不使用此则可能共用一把锁，造成任务丢失
	 *  此锁定名称仅在 @MeeTimeds 内不重复即可
	 */
	String lockName()  default "";

	/**
	 * How long the lock should be kept in case the machine which obtained the lock died before releasing it.
	 * This is just a fallback, under normal circumstances the lock is released as soon the tasks finishes. Can be any format
	 * supported by <a href="https://docs.micronaut.io/latest/guide/config.html#_duration_conversion">Duration Conversion</a>
	 * <p>
	 * 最大锁定时间：最大锁定时间为保护任务在最坏的情况下的解锁时间，比如任务实际执行时间超过了最低锁定时间(lockAtLeastFor),如果在最大锁定时间仍未执行完成则会自动解锁
	 */
	String lockAtMostFor() default "";

	/**
	 * The lock will be held at least for this period of time. Can be used if you really need to execute the task
	 * at most once in given period of time. If the duration of the task is shorter than clock difference between nodes, the task can
	 * be theoretically executed more than once (one node after another). By setting this parameter, you can make sure that the
	 * lock will be kept at least for given period of time. Can be any format
	 * supported by <a href="https://docs.micronaut.io/latest/guide/config.html#_duration_conversion">Duration Conversion</a>
	 * 最低锁定时间：如果执行时间<最低锁定时间 则下一次任务只可以在穗子锁定时间后执行
	 * 			   如果不设置则它的锁定时间即为执行时间(每次执行完成后会解锁)
	 */
	String lockAtLeastFor() default "";

}
