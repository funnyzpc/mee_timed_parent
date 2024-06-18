//package com.mee.timed.template;
//
//
//@Deprecated
//public abstract class AbstractSimpleLock implements SimpleLock {
//    private boolean valid = true;
//    protected final LockConfiguration lockConfiguration;
//
//    protected AbstractSimpleLock(LockConfiguration lockConfiguration) {
//        this.lockConfiguration = lockConfiguration;
//    }
//
//    @Override
//    public final void unlock() {
//        checkValidity();
//        doUnlock();
//        valid = false;
//    }
//
//    protected abstract void doUnlock();
//
////    @Override
////    @Deprecated
////    public final Optional<SimpleLock> extend(Instant lockAtMostUntil, Instant lockAtLeastUntil) {
////        Instant now = Instant.now();
////        return extend(Duration.between(now, lockAtMostUntil), Duration.between(now, lockAtLeastUntil));
////    }
//
////    @Override
////    public Optional<SimpleLock> extend(Duration lockAtMostFor, Duration lockAtLeastFor) {
////        checkValidity();
////        Optional<SimpleLock> result = doExtend(new LockConfiguration(ClockProvider.now(), lockConfiguration.getName(), lockAtMostFor, lockAtLeastFor));
////        valid = false;
////        return result;
////    }
////
////    protected Optional<SimpleLock> doExtend(LockConfiguration newConfiguration) {
////        throw new UnsupportedOperationException();
////    }
//
//    private void checkValidity() {
//        if (!valid) {
//            throw new IllegalStateException("Lock " + lockConfiguration.getName() + " is not valid, it has already been unlocked or extended");
//        }
//    }
//}
