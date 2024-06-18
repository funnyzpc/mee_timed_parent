package com.mee.timed.template;


import java.util.Optional;

/**
 * Provides lock implementation.
 */
public interface LockProvider {

    /**
     * @return If empty optional has been returned, lock could not be acquired. The lock
     * has to be released by the callee.
     */
    Optional<SimpleLock> lock( LockConfiguration lockConfiguration);

    StorageAccessor getStorageAccessor();
    Configuration getConfiguration() ;
}
