/**
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.shedlock.provider.jdbctemplate;

import net.javacrumbs.shedlock.core.ClockProvider;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.test.support.jdbc.PostgresConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

import static java.lang.Thread.sleep;
import static net.javacrumbs.shedlock.core.ClockProvider.now;
import static net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider.Configuration.builder;
import static org.assertj.core.api.Assertions.assertThat;

public class PostgresJdbcTemplateLockProviderIntegrationTest extends AbstractJdbcTemplateLockProviderIntegrationTest {
    private static final PostgresConfig dbConfig = new PostgresConfig();

    protected PostgresJdbcTemplateLockProviderIntegrationTest() {
        super(dbConfig);
    }

    @BeforeAll
    public static void startDb() {
        dbConfig.startDb();
    }

    @AfterAll
    public static void shutdownDb() {
        dbConfig.shutdownDb();
    }

    @Nested
    class TimezoneTest {
        @AfterEach
        void resetClock() {
            ClockProvider.setClock(Clock.systemDefaultZone());
        }

        @Test
        void shouldHonorTimezone() {
            TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");

            Instant lockUntil = Instant.parse("2020-04-10T17:30:00Z");
            ClockProvider.setClock(Clock.fixed(lockUntil.minusSeconds(10), timezone.toZoneId()));

            TimeZone originalTimezone = TimeZone.getDefault();


            DataSource datasource = dbConfig.getDataSource();

            TimeZone.setDefault(timezone);

            try {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
//                jdbcTemplate.execute("CREATE TABLE SHEDLOCK ( APPLICATION VARCHAR(64) NOT NULL , NAME varchar(64) not null, HOST_IP varchar(32) not null, LOCKED_AT timestamp(6) NOT NULL, LOCK_UNTIL timestamp(6) NOT NULL, LOCKED_BY varchar(100) NOT NULL, LABEL varchar(100), STATE CHAR(1) NOT NULL DEFAULT 1, PRIMARY KEY (APPLICATION, NAME));\n");
//                jdbcTemplate.execute("CREATE TABLE SYS_SHEDLOCK_APP ( APPLICATION VARCHAR(64) NOT NULL , HOST_IP VARCHAR(32) not null,HOST_NAME varchar(100),STATE CHAR(1) NOT NULL DEFAULT 1,CREATE_TIME TIMESTAMP NOT NULL,PRIMARY KEY (APPLICATION,HOST_IP));\n");
                jdbcTemplate.execute("CREATE TABLE SYS_SHEDLOCK_JOB ( APPLICATION VARCHAR(64) NOT NULL , NAME varchar(64) not null, HOST_IP varchar(32) not null, LOCKED_AT timestamp(6) NOT NULL, LOCK_UNTIL timestamp(6) NOT NULL, LOCKED_BY varchar(100) NOT NULL, LABEL varchar(100), STATE CHAR(1) NOT NULL DEFAULT 1, PRIMARY KEY (APPLICATION, NAME));\n");

                JdbcTemplateLockProvider provider = new JdbcTemplateLockProvider(builder()
                    .withJdbcTemplate(new JdbcTemplate(datasource))
                    .withTableName("SYS_SHEDLOCK_JOB")
                    .withTimeZone(timezone)
                    .withIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)
                    .build());

                LockConfiguration.initProperties("shedlock-provider-jdbc-template");
                provider.lock(new LockConfiguration(lockUntil,"timezone_test",Duration.ofSeconds(1),Duration.ofSeconds(1)));
                new JdbcTemplate(datasource).query("SELECT * FROM SYS_SHEDLOCK_JOB where APPLICATION='shedlock-provider-jdbc-template' and name='timezone_test'", rs -> {
                    Timestamp timestamp = rs.getTimestamp("lock_until");
                    assertThat(timestamp.getTimezoneOffset()).isEqualTo(7 * 60);
                    assertThat(timestamp.toInstant()).isEqualTo(lockUntil);
                });
            } finally {
                TimeZone.setDefault(originalTimezone);
            }
        }
    }

    @Nested
    class StorageAccessor extends AbstractJdbcTemplateStorageAccessorTest {
        private static final String MY_LOCK = "my-lock";
        private static final String OTHER_LOCK = "other-lock";

        protected StorageAccessor() {
            super(dbConfig);
        }

        @Test
        void shouldUpdateOnInsertAfterValidityOfPreviousEndedWhenNotUsingDbTime() throws InterruptedException {
            shouldUpdateOnInsertAfterValidityOfPreviousEnded(false);
        }

        @Test
        void shouldUpdateOnInsertAfterValidityOfPreviousEndedWhenUsingDbTime() throws InterruptedException {
            shouldUpdateOnInsertAfterValidityOfPreviousEnded(true);
        }

        private void shouldUpdateOnInsertAfterValidityOfPreviousEnded(boolean usingDbTime) throws InterruptedException {
            JdbcTemplateStorageAccessor accessor = getAccessor(usingDbTime);
            LockConfiguration.initProperties("shedlock-provider-jdbc-template");
            accessor.insertRecord(new LockConfiguration(now(), OTHER_LOCK, Duration.ofSeconds(5), Duration.ZERO));
            Timestamp otherLockValidity = getTestUtils().getLockedUntil(OTHER_LOCK);

            assertThat(
                accessor.insertRecord(new LockConfiguration(now(), MY_LOCK, Duration.ofMillis(10), Duration.ZERO))
            ).isEqualTo(true);

            sleep(10);

            assertThat(
                accessor.insertRecord(new LockConfiguration(now(), MY_LOCK, Duration.ofMillis(10), Duration.ZERO))
            ).isEqualTo(true);

            // check that the other lock has not been affected by "my-lock" update
            assertThat(getTestUtils().getLockedUntil(OTHER_LOCK)).isEqualTo(otherLockValidity);
        }
    }

}
