package com.mee.timed.template;

class PostgresSqlStatementsSource extends SqlStatementsSource {
    PostgresSqlStatementsSource(Configuration configuration) {
        super(configuration);
    }

    @Override
    String getInsertStatement() {
        // SqlStatementsSource::getInsertStatement
        // INSERT INTO shedlock(name, lock_until, locked_at, locked_by) VALUES(:name, :lockUntil, :now, :lockedBy)
        //      ON CONFLICT (name) DO UPDATE SET lock_until = :lockUntil, locked_at = :now, locked_by = :lockedBy WHERE shedlock.lock_until <= :now
        return super.getInsertStatement() + " ON CONFLICT (" + application() +" , "+ name() + ") DO UPDATE " +
            "SET " + lockUntil() + " = :lockUntil, " + lockedAt() + " = :now, " + lockedBy() + " = :lockedBy, " +hostIP()+" = :hostIP, " +updateTime()+" = CURRENT_TIMESTAMP "+
            "WHERE " + tableName() + "." + lockUntil() + " <= :now AND " + tableName() + "."+state()+" = :state " +
            super.appCause();
    }

    @Override
    String getAppInsertStatement() {
        // SqlStatementsSource::getInsertStatement
        // INSERT INTO shedlock(name, lock_until, locked_at, locked_by) VALUES(:name, :lockUntil, :now, :lockedBy)
        //      ON CONFLICT (name) DO UPDATE SET lock_until = :lockUntil, locked_at = :now, locked_by = :lockedBy WHERE shedlock.lock_until <= :now
        return super.getAppInsertStatement() + " ON CONFLICT (" + application() +" , "+ hostIP() + ") DO UPDATE " +
            "SET "+ hostName() + " = :hostName, " + appUpdateTime() + " = CURRENT_TIMESTAMP " +
            "WHERE (" + tableAppName() + "." + hostName() + " != :hostName OR " +tableAppName() + "." + appUpdateTime() + " != CURRENT_TIMESTAMP ) AND " +tableAppName()+"."+state()+" = :state";

    }

}
