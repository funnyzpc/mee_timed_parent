package com.mee.timed.template;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Spring JdbcTemplate based implementation usable in JTA environment
 */
public class JdbcTemplateStorageAccessor implements StorageAccessor {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Configuration configuration;
    private SqlStatementsSource sqlStatementsSource;
    private final boolean isNeedApp;
    // 这里比较危险，如果是当前类是多例建议用static修饰此参数
//    private static boolean isAlreadyCreatedApp = false;
//    private final String application;


    JdbcTemplateStorageAccessor(Configuration configuration) {
        requireNonNull(configuration, "configuration can not be null");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(configuration.getJdbcTemplate());
        this.configuration = configuration;
//        this.application=configuration.getApplication();
//        PlatformTransactionManager transactionManager = configuration.getTransactionManager() != null ?
//            configuration.getTransactionManager() :
//            new DataSourceTransactionManager(configuration.getJdbcTemplate().getDataSource());

//        this.transactionTemplate = new TransactionTemplate(transactionManager);
//        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//
//        if (configuration.getIsolationLevel() != null) {
//            this.transactionTemplate.setIsolationLevel(configuration.getIsolationLevel());
//        }
        // 补充创建标志位
        isNeedApp = configuration.hasAppTable();
    }

//    @Deprecated
//    @Override
//    public boolean insertRecord(LockConfiguration lockConfiguration) {
//        try {
//            String sql = sqlStatementsSource().getInsertStatement();
//            logger.debug("shedlock插入语句:{},{}",sql,null);
//            if( isNeedApp && !isAlreadyCreatedApp ){
//                // 这里保证在当前类单例下也能保持同步，同时保证isAlreadyCreatedApp的可见性 synchronized is for oracle DB
//                synchronized (this) {
//                    String appSql = sqlStatementsSource().getAppInsertStatement();
//                    try {
//                        isAlreadyCreatedApp = execute(appSql, lockConfiguration);
//                    } catch (Exception e) {
//                        logger.error("shedlock_app插入语句错误:{},{},{}", appSql, lockConfiguration, isAlreadyCreatedApp, e);
//                    }
//                    logger.debug("shedlock_app插入语句:{},{},{}", appSql, lockConfiguration, isAlreadyCreatedApp);
//                }
//            }
//            return execute(sql, lockConfiguration);
//        } catch (DuplicateKeyException | CannotSerializeTransactionException e) {
//            logger.debug("Duplicate key", e);
//            return false;
//        } catch (DataIntegrityViolationException | BadSqlGrammarException | UncategorizedSQLException | TransactionSystemException e) {
//            logger.error("Unexpected exception", e);
//            return false;
//        }
//    }

    @Override
    public boolean insertAppRecord(LockConfiguration lockConfiguration) {
        try {
            String sql = null;
            if(this.isNeedApp && execute(sql=sqlStatementsSource().getAppInsertStatement(), lockConfiguration)){
                LOGGER.debug("已创建app数据:{},{}",sql,lockConfiguration);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            LOGGER.debug("异常 key", e);
            return false;
        }
    }

    @Override
    public boolean insertJobRecord(LockConfiguration lockConfiguration) {
        try {
            String sql = sqlStatementsSource().getInsertStatement();
            if(execute(sql, lockConfiguration)){
                LOGGER.debug("已创建job数据:{},{}",sql,lockConfiguration);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }  catch (Exception e) {
            LOGGER.debug("异常 key", e);
            return false;
        }
    }

    @Override
    public boolean updateRecord(LockConfiguration lockConfiguration) {
        String sql = sqlStatementsSource().getUpdateStatement();
        try {
            LOGGER.debug("shedlock更新语句:{},{}",sql,lockConfiguration);
            return execute(sql, lockConfiguration);
        } catch (CannotSerializeTransactionException e) {
            LOGGER.debug("Serialization exception", e);
            return false;
        } catch (DataIntegrityViolationException | TransactionSystemException e) {
            LOGGER.error("Unexpected exception", e);
            return false;
        }
    }

//    @Override
//    public boolean extend(LockConfiguration lockConfiguration) {
//        String sql = sqlStatementsSource().getExtendStatement();
//
//        logger.debug("Extending lock={} until={}", lockConfiguration.getName(), lockConfiguration.getLockAtMostUntil());
//        return execute(sql, lockConfiguration);
//    }

    @Override
    public void unlock(LockConfiguration lockConfiguration) {
        try {
            doUnlock(lockConfiguration);
        } catch (TransactionSystemException e) {
            LOGGER.info("Unlock failed due to TransactionSystemException - retrying");
            doUnlock(lockConfiguration);
        }
    }

    private void doUnlock(LockConfiguration lockConfiguration) {
        String sql = sqlStatementsSource().getUnlockStatement();
        execute(sql, lockConfiguration);
    }

    private boolean execute(String sql, LockConfiguration lockConfiguration) throws TransactionException {
        return jdbcTemplate.update(sql, params(lockConfiguration)) > 0;
    }

    private Map<String, Object> params(LockConfiguration lockConfiguration) {
        return sqlStatementsSource().params(lockConfiguration);
    }

    private SqlStatementsSource sqlStatementsSource() {
        synchronized (configuration) {
            if (sqlStatementsSource == null) {
                sqlStatementsSource = SqlStatementsSource.create(configuration);
            }
            return sqlStatementsSource;
        }
    }

//    @Override
//    public String getApplication() {
//        return this.application;
//    }

//    @Override
//    public boolean isNeedApp() {
//        return isNeedApp;
//    }
}
