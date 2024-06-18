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

class PostgresSqlStatementsSource extends SqlStatementsSource {
    PostgresSqlStatementsSource(JdbcTemplateLockProvider.Configuration configuration) {
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
        return super.getAppInsertStatement() + " ON CONFLICT (" + appApplication() +" , "+ appHostIP() + ") DO UPDATE " +
            "SET "+ appHostName() + " = :hostName, " + appUpdateTime() + " = CURRENT_TIMESTAMP " +
            "WHERE (" + appTableName() + "." + appHostName() + " != :hostName OR " + appTableName() + "." + appUpdateTime() + " != CURRENT_TIMESTAMP ) AND " + appTableName()+"."+appState()+" = :state";

    }

}
