
package com.mee.timed.template;


import java.time.Duration;
import java.time.Instant;
import java.util.Objects;


/**
 * Lock configuration.
 */
public class LockConfiguration {
    private Instant createdAt;

    private final String name;

    /**
     * The lock is held until this duration passes, after that it's automatically released (the process holding it has most likely
     * died without releasing the lock) Can be ignored by providers which can detect dead processes (like Zookeeper)
     * 锁的持续时间时间，存表为 createdAt+lockAtMostFor
     */
    private final Duration lockAtMostFor;

    /**
     * The lock will be held at least this duration even if the task holding the lock finishes earlier.
     * 即使持有锁的任务提前完成，锁也将至少持有此持续时间。
     */
    private final Duration lockAtLeastFor;
//    /**
//     * 应用名称
//     */
//    private static String schedName;
//
//
//    /**
//     * @deprecated please use {@link #LockConfiguration(Instant, String, Duration, Duration)}
//     */
//    @Deprecated
//    public LockConfiguration(String name, Duration lockAtMostFor, Duration lockAtLeastFor) {
//        this(now(), name, lockAtMostFor, lockAtLeastFor);
//    }
//
//    /**
//     * @deprecated please use {@link #LockConfiguration(Instant, String, Duration, Duration)}
//     */
//    @Deprecated
//    public LockConfiguration(String name, Instant lockAtMostUntil) {
//        this(name, lockAtMostUntil, now());
//    }
//
//    /**
//     * @deprecated please use {@link #LockConfiguration(Instant, String, Duration, Duration)}
//     */
//    @Deprecated
//    public LockConfiguration(String name, Instant lockAtMostUntil, Instant lockAtLeastUntil) {
//        this(now(), name, lockAtMostUntil, lockAtLeastUntil);
//    }
//
//    /**
//     * @deprecated please use {@link #LockConfiguration(Instant, String, Duration, Duration)}
//     */
//    @Deprecated
//    private LockConfiguration(Instant createdAt, String name, Instant lockAtMostUntil, Instant lockAtLeastUntil) {
//        this(createdAt, name, Duration.between(createdAt, lockAtMostUntil), Duration.between(createdAt, lockAtLeastUntil));
//    }

    /**
     * Creates LockConfiguration. There are two types of lock providers. One that uses "db time" which requires relative
     * values of lockAtMostFor and lockAtLeastFor (currently it's only JdbcTemplateLockProvider). Second type of
     * lock provider uses absolute time calculated from `createdAt`.
     *
     * @param createdAt
     * @param name
     * @param lockAtMostFor
     * @param lockAtLeastFor
     */
    public LockConfiguration(Instant createdAt, String name, Duration lockAtMostFor, Duration lockAtLeastFor) {
        this.createdAt = Objects.requireNonNull(createdAt);
        this.name = Objects.requireNonNull(name);
        this.lockAtMostFor = Objects.requireNonNull(lockAtMostFor);
        this.lockAtLeastFor = Objects.requireNonNull(lockAtLeastFor);
        if (lockAtLeastFor.compareTo(lockAtMostFor) > 0) {
            throw new IllegalArgumentException("lockAtLeastFor is longer than lockAtMostFor for lock '" + name + "'.");
        }
        if (lockAtMostFor.isNegative()) {
            throw new IllegalArgumentException("lockAtMostFor is negative '" + name + "'.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("lock name can not be empty");
        }
    }


    public String getName() {
        return name;
    }

    public Instant getLockAtMostUntil() {
        return createdAt.plus(lockAtMostFor);
    }

    public Instant getLockAtLeastUntil() {
        return createdAt.plus(lockAtLeastFor);
    }

    /**
     * Returns either now or lockAtLeastUntil whichever is later.
     */
    public Instant getUnlockTime() {
        Instant now = ClockProvider.now();
        Instant lockAtLeastUntil = getLockAtLeastUntil();
        return lockAtLeastUntil.isAfter(now) ? lockAtLeastUntil : now;
    }

    public Duration getLockAtLeastFor() {
        return lockAtLeastFor;
    }

    public Duration getLockAtMostFor() {
        return lockAtMostFor;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
//    public static String getSchedName() {
//        return schedName;
//    }
//
//    /**
//     * 初始化配置参数
//     */
//    public static void initProperties(String schedName){
//        LockConfiguration.schedName = schedName;
//    }

    @Override
    public String toString() {
        return "LockConfiguration{" +
            "createdAt=" + createdAt +
            ", name='" + name + '\'' +
            ", lockAtMostFor=" + lockAtMostFor +
            ", lockAtLeastFor=" + lockAtLeastFor +
            '}';
    }
}
