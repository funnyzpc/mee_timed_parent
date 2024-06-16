package com.mee.timed.template;


public interface StorageAccessor {
    /**
     * Inserts a record, if it does not already exists. If it exists, returns false.
     *
     * @param lockConfiguration LockConfiguration
     * @return true if inserted
     */
//    @Deprecated
//    boolean insertRecord( LockConfiguration lockConfiguration);
    boolean insertAppRecord( LockConfiguration lockConfiguration);
    boolean insertJobRecord( LockConfiguration lockConfiguration);

    /**
     * Tries to update the lock record. If there is already a valid lock record (the lock is held by someone else)
     * update should not do anything and this method returns false.
     *
     * @param lockConfiguration LockConfiguration
     * @return true if updated
     */
    boolean updateRecord( LockConfiguration lockConfiguration);

    void unlock( LockConfiguration lockConfiguration);

//    default boolean extend( LockConfiguration lockConfiguration) {
//        throw new UnsupportedOperationException();
//    }

//    String getApplication();
//    boolean isNeedApp();

}
