
package com.mee.timed.template;


import java.util.Optional;

/**
 * Distributed lock using abstract storage
 * <p>
 * It uses a table/collection that contains ID = lock name and a field locked_until.
 * <ol>
 * <li>
 * Attempts to insert a new lock record. As an optimization, we keep in-memory track of created lock records. If the record
 * has been inserted, returns lock.
 * </li>
 * <li>
 * We will try to update lock record using filter ID == name AND lock_until &lt;= now
 * </li>
 * <li>
 * If the update succeeded (1 updated row/document), we have the lock. If the update failed (0 updated documents) somebody else holds the lock
 * </li>
 * <li>
 * When unlocking, lock_until is set to now.
 * </li>
 * </ol>
 */
public class JdbcStorageBasedLockProvider implements LockProvider {
    
    private final StorageAccessor storageAccessor;
    private final Configuration configuration;
//    private final LockRecordRegistry lockRecordRegistry = new LockRecordRegistry();

    public JdbcStorageBasedLockProvider( Configuration configuration) {
        this.configuration=configuration;
        this.storageAccessor = new JdbcTemplateStorageAccessor(configuration);
    }

    @Override
    public StorageAccessor getStorageAccessor() {
        return storageAccessor;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
//    @Deprecated
//    public JdbcStorageBasedLockProvider( StorageAccessor storageAccessor) {
//        this.storageAccessor = storageAccessor;
//    }

//    /**
//     * Clears cache of existing lock records.
//     * 清理锁缓存 测试用
//     */
//    public void clearCache() {
//        lockRecordRegistry.clear();
//    }

    @Override
    public Optional<SimpleLock> lock( LockConfiguration lockConfiguration) {
        boolean lockObtained = doLock(lockConfiguration);
        if (lockObtained) {
            return Optional.of(new StorageLock(lockConfiguration, storageAccessor));
        } else {
            return Optional.empty();
        }
    }
    /**
     * Sets lockUntil according to LockConfiguration if current lockUntil &lt;= now
     * 当 lock_until 时间小于等于当前和时间则
     */
    private boolean doLock(LockConfiguration lockConfiguration) {
        // let's try to update the record, if successful, we have the lock
        // 尝试更细记录，更新成功即获得锁
        try {
            return storageAccessor.updateRecord(lockConfiguration);
        } catch (Exception e) {
            throw e;
        }
    }

//    /**
//     * Sets lockUntil according to LockConfiguration if current lockUntil &lt;= now
//     * 当 lock_until 时间小于等于当前和时间则
//     */
//    private boolean doLock(LockConfiguration lockConfiguration) {
////        final String name = lockConfiguration.getName();
//        final String name = storageAccessor.getApplication()+"#"+lockConfiguration.getName();
//        boolean tryToCreateLockRecord = !lockRecordRegistry.lockRecordRecentlyCreated(name);
//        if (tryToCreateLockRecord) {
//            // create record in case it does not exist yet
//            storageAccessor.insertRecord(lockConfiguration);
////            if (storageAccessor.insertRecord(lockConfiguration)) {
////                lockRecordRegistry.addLockRecord(name);
////                // we were able to create the record, we have the lock
////                return true;
////            }
//            // we were not able to create the record, it already exists, let's put it to the cache so we do not try again
//            lockRecordRegistry.addLockRecord(name);
//        }
//
//        // let's try to update the record, if successful, we have the lock
//        // 尝试更细记录，更新成功即获得锁
//        try {
//            return storageAccessor.updateRecord(lockConfiguration);
//        } catch (Exception e) {
//            // There are some users that start the app before they have the DB ready.
//            // If they use JDBC, insertRecord returns false, the record is stored in the recordRegistry
//            // and the insert is not attempted again. We are assuming that the DB still does not exist
//            // when update is attempted. Unlike insert, update throws the exception, and we clear the cache here.
//            if (tryToCreateLockRecord) {
//                lockRecordRegistry.removeLockRecord(name);
//            }
//            throw e;
//        }
//    }

//    private static class StorageLock extends AbstractSimpleLock {
//        private final StorageAccessor storageAccessor;
//
//        StorageLock(LockConfiguration lockConfiguration, StorageAccessor storageAccessor) {
//            super(lockConfiguration);
//            this.storageAccessor = storageAccessor;
//        }
//
//        @Override
//        public void doUnlock() {
//            storageAccessor.unlock(lockConfiguration);
//        }
//
////        @Override
////        public Optional<SimpleLock> doExtend(LockConfiguration newConfig) {
////            if (storageAccessor.extend(newConfig)) {
////                return Optional.of(new StorageLock(newConfig, storageAccessor));
////            } else {
////                return Optional.empty();
////            }
////        }
//
//    }

}
