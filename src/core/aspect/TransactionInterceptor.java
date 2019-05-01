package core.aspect;

import java.sql.Connection;
import java.sql.SQLException;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.ibatis.sqlmap.client.SqlMapClient;

import core.exception.ApplicationException;
import core.service.GenericService;

public class TransactionInterceptor implements org.aopalliance.intercept.MethodInterceptor {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        if (!(methodInvocation.getThis() instanceof GenericService)) {
            throw new RuntimeException("Only objects that extends " + GenericService.class +  " can have methods intercepted");            
        }

        Object result = null;
        SqlMapClient sqlMap = null;
        String isolation = null;
        
        Transactional transactional = methodInvocation.getMethod().getAnnotation(Transactional.class);

        if (transactional != null) {

            if (!GenericService.isInTransaction()) {
                GenericService service = (GenericService) methodInvocation.getThis();
                sqlMap = service.getSqlMap();
                
                
                if (service.isSwallowException()) {
                    
                    try {
                        isolation = transactional.isolation();
                        
                        if ("none".equals(isolation)) {                            
                            sqlMap.startTransaction(Connection.TRANSACTION_NONE);
                        } else if ("read_commit".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_READ_COMMITTED);
                        } else if ("read_uncommit".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_READ_UNCOMMITTED);
                        } else if ("repeat".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_REPEATABLE_READ);
                        } else if ("serial".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_SERIALIZABLE);
                        } else {
                            sqlMap.startTransaction();
                        }
                        service.startTransaction();    
                        
                        if (logger.isDebugEnabled()) {
                            logger.debug(Thread.currentThread()  + "["+methodInvocation.getMethod()+"] Transaction started" + (isolation != null ? " (" + isolation +")" : ""));
                        }                        

                        result = methodInvocation.proceed();
                                       
                        sqlMap.commitTransaction();       
                        
                        if (logger.isDebugEnabled()) {
                            logger.debug(Thread.currentThread()  + "["+methodInvocation.getMethod()+"] Transaction committed");
                        }
                        
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();                        
                    } finally {                       
                        service.endTransaction();
                        try {
                            sqlMap.endTransaction();
                            
                            if (logger.isDebugEnabled()) {
                                logger.debug(Thread.currentThread()  + "["+methodInvocation.getMethod()+"] Transaction ended");
                            }
                            
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }                
                    
                } else {
                
                    try {
                        isolation = transactional.isolation();
                        
                        if ("none".equals(isolation)) {                            
                            sqlMap.startTransaction(Connection.TRANSACTION_NONE);
                        } else if ("read_commit".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_READ_COMMITTED);
                        } else if ("read_uncommit".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_READ_UNCOMMITTED);
                        } else if ("repeat".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_REPEATABLE_READ);
                        } else if ("serial".equals(isolation)) {
                            sqlMap.startTransaction(Connection.TRANSACTION_SERIALIZABLE);
                        } else {
                            sqlMap.startTransaction();
                        }
                        service.startTransaction();
                        
                        if (logger.isDebugEnabled()) {
                            logger.debug(Thread.currentThread()  + "["+methodInvocation.getMethod()+"] Transaction started" + (isolation != null ? " (" + isolation +")" : ""));
                        }                        
    
                        result = methodInvocation.proceed();                        
                                        
                        sqlMap.commitTransaction();
                        
                        if (logger.isDebugEnabled()) {
                            logger.debug(Thread.currentThread()  + "["+methodInvocation.getMethod()+"] Transaction committed");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new ApplicationException(e);
                    } finally {                       
                        service.endTransaction();
                        try {
                            sqlMap.endTransaction();
                            
                            if (logger.isDebugEnabled()) {
                                logger.debug(Thread.currentThread()  + "["+methodInvocation.getMethod()+"] Transaction ended");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new ApplicationException(e);
                        }
                    }                
                    
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(Thread.currentThread() + "["+methodInvocation.getMethod()+"] Reusing transaction begin");
                }
                result = methodInvocation.proceed();
                if (logger.isDebugEnabled()) {
                    logger.debug(Thread.currentThread() + "["+methodInvocation.getMethod()+"] Reusing transaction finish");
                }
            }                
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug(Thread.currentThread() + "["+methodInvocation.getMethod()+"] Not annotated");
            }
            result = methodInvocation.proceed();
        }            
        return result;            
    }
}

