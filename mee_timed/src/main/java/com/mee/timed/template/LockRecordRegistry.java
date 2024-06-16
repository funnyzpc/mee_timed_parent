package com.mee.timed.template;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Some LockProviders have to decide if a new record has to be created or an old one updated.
 * This class helps them keep track of existing lock records, so they know if a lock record exists.
 * 一些锁定提供者必须决定是创建新记录还是更新旧记录。
 * 这个类帮助他们跟踪现有的锁定记录，这样他们就可以知道是否存在锁定记录。
 */
@Deprecated
public final class LockRecordRegistry {
    /**
     * 这是锁对象的cache
     */
    private final Set<String> lockRecords = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    /**
     * 设定一个锁
     * @param lockName 锁名称
     */
    public void addLockRecord(String lockName) {
        lockRecords.add(lockName);
    }

    /**
     * 移除锁
     * @param lockName 锁名称
     */
    void removeLockRecord(String lockName) {
        lockRecords.remove(lockName);
    }

    /**
     * 查看对象缓存内是否有对应的锁
     * @param lockName 锁名称
     * @return true/false
     */
    public boolean lockRecordRecentlyCreated(String lockName) {
        return lockRecords.contains(lockName);
    }

    /**
     * 获取锁的大小
     */
    int getSize() {
        return lockRecords.size();
    }

    /**
     * 清理锁缓存
     */
    public void clear() {
        lockRecords.clear();
    }
}
