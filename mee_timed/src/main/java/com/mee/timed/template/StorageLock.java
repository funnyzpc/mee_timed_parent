package com.mee.timed.template;

/**
 * StorageLock
 *
 * @author shaoow
 * @version 1.0
 * @className StorageLock
 * @date 2024/6/18 11:15
 */
public final class StorageLock /*extends AbstractSimpleLock*/ implements SimpleLock {
    private boolean valid = true;
    protected final LockConfiguration lockConfiguration;
    private final StorageAccessor storageAccessor;

    StorageLock(LockConfiguration lockConfiguration, StorageAccessor storageAccessor) {
//        super(lockConfiguration);
        this.lockConfiguration = lockConfiguration;
        this.storageAccessor = storageAccessor;
    }

    @Override
    public void unlock() {
        checkValidity();
        doUnlock();
        valid = false;
    }

    @Override
    public void doUnlock() {
        storageAccessor.unlock(lockConfiguration);
    }

    private void checkValidity() {
        if (!valid) {
            throw new IllegalStateException("Lock " + lockConfiguration.getName() + " is not valid, it has already been unlocked or extended");
        }
    }

//        @Override
//        public Optional<SimpleLock> doExtend(LockConfiguration newConfig) {
//            if (storageAccessor.extend(newConfig)) {
//                return Optional.of(new StorageLock(newConfig, storageAccessor));
//            } else {
//                return Optional.empty();
//            }
//        }
}
