
package com.mee.timed.config;

import com.mee.timed.data.MeeTimedProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

/**
 * {@code @Configuration} class that registers a {@link ScheduledAnnotationBeanPostProcessor}
 * bean capable of processing Spring's @{@link Scheduled} annotation.
 *
 * <p>This configuration class is automatically imported when using the
 * {@link org.springframework.scheduling.annotation.EnableScheduling @EnableScheduling} annotation. See
 * {@code @EnableScheduling}'s javadoc for complete usage details.
 *
 * SchedulingConfiguration
 * @author Chris Beams
 * @since 3.1
 * @see EnableScheduling
 * @see MeeTimedAnnotationBeanPostProcessor
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
// 必须在这些Spring Bean创建之后
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, LiquibaseAutoConfiguration.class, FlywayAutoConfiguration.class })
@EnableConfigurationProperties(MeeTimedProperties.class)
public class MeeTimedConfiguration {

//	@Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
	@Bean(name ="com.mee.task.scheduling.MeeTimedConfiguration.annotationProcessor")
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//	@ConditionalOnSingleCandidate(DataSource.class)
	public MeeTimedAnnotationBeanPostProcessor annotationProcessor(DataSource dataSource,MeeTimedProperties properties) {
		return new MeeTimedAnnotationBeanPostProcessor()
				.setJdbcTemplate(new JdbcTemplate(dataSource))
				.setProperties(properties);
	}


//	@Bean
////	@ConditionalOnSingleCandidate(DataSource.class)
//	public SchedulerFactoryBean timeds(MeeTimedProperties properties,ApplicationContext applicationContext,javax.sql.DataSource dataSource) {
//		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
////		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
////		jobFactory.setApplicationContext(applicationContext);
////		schedulerFactoryBean.setJobFactory(jobFactory);
////		schedulerFactoryBean.setAutoStartup(properties.isAutoStartup());
//
////		schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(properties.isWaitForJobsToCompleteOnShutdown());
////		schedulerFactoryBean.setOverwriteExistingJobs(properties.isOverwriteExistingJobs());
//		if ( null!=properties ) {
//			schedulerFactoryBean.setMeeTimedProperties(properties);
//			schedulerFactoryBean.setSchedulerName(properties.getSchedName());
//			// 延迟启动时间
//			schedulerFactoryBean.setStartupDelay(properties.getStartupDelay());
//		}
////		schedulerFactoryBean.setCalendars(calendars);
////		schedulerFactoryBean.setTriggers(triggers.orderedStream().toArray(Trigger[]::new));
////		customizers.orderedStream().forEach((customizer) -> customizer.customize(schedulerFactoryBean));
//		schedulerFactoryBean.setDataSource(dataSource);
//		new JdbcTemplate(dataSource);
//		return schedulerFactoryBean;
//	}


}
