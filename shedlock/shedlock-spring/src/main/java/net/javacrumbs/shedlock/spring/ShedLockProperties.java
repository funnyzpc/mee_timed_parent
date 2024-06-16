package net.javacrumbs.shedlock.spring;

import net.javacrumbs.shedlock.core.LockConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

/**
 * ShedlockProperties
 *
 * @author shaoow
 * @version 1.0
 * @className ShedLockProperties
 * @date 2024/5/31 14:28
 */
public class ShedLockProperties implements InitializingBean {

    private static final String SCHED_KEY = "spring.shedlock.properties.sched";
    private static final String APPLICATION_NAME_KEY = "spring.application.name";

    private final String schedName;

    public ShedLockProperties(@Value("${"+SCHED_KEY+":}") String sched,
                              @Value("${"+APPLICATION_NAME_KEY+":}") String applicationName,
                              Properties properties) {
        String schedNameTmp = !"".equals(sched)?sched: applicationName;
        if( "".equals(schedNameTmp) && null!=properties && (schedNameTmp = (String) properties.get(SCHED_KEY))==null && (schedNameTmp = (String) properties.get(APPLICATION_NAME_KEY))==null ){
            throw new IllegalArgumentException("need param:"+SCHED_KEY+" or "+APPLICATION_NAME_KEY);
        }
        this.schedName=schedNameTmp;
    }

    public String getSchedName() {
        return this.schedName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LockConfiguration.initProperties(this.getSchedName());
    }
}
