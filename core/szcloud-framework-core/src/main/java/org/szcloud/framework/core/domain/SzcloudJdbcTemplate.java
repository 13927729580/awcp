package org.szcloud.framework.core.domain;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class SzcloudJdbcTemplate extends org.springframework.jdbc.core.JdbcTemplate {
		 
	    private PlatformTransactionManager platformTransactionManager;
	    private DefaultTransactionDefinition transactionDefinition;
	    private ThreadLocal<TransactionStatus> transcationStatus = new ThreadLocal<TransactionStatus>();
	 
	    public void beginTranstaion() {
	        TransactionStatus tmp = platformTransactionManager.getTransaction(transactionDefinition);
	        transcationStatus.set(tmp);
	    }
	 
	    public void commit() throws Exception {
	        TransactionStatus tmp = transcationStatus.get();
	        if (tmp == null) {
	            throw new Exception("no transcation");
	        }
	        platformTransactionManager.commit(tmp);
	        transcationStatus.remove();
	    }
	 
	    public void rollback() throws Exception {
	        TransactionStatus tmp = transcationStatus.get();
	        if (tmp == null) {
	            throw new Exception("no transcation");
	        }
	        platformTransactionManager.rollback(tmp);
	        transcationStatus.remove();
	 
	    }
	 
	    @Override
	    public void afterPropertiesSet() {
	        super.afterPropertiesSet();
	        transactionDefinition = new DefaultTransactionDefinition();
	        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	        platformTransactionManager = new DataSourceTransactionManager(getDataSource());
	 
	    }
	 
	    public PlatformTransactionManager getPlatformTransactionManager() {
	        return platformTransactionManager;
	    }
	 
	    public DefaultTransactionDefinition getTransactionDefinition() {
	        return transactionDefinition;
	    }
	 
	    public ThreadLocal<TransactionStatus> getTranscationStatus() {
	        return transcationStatus;
	    }
	 
	}
