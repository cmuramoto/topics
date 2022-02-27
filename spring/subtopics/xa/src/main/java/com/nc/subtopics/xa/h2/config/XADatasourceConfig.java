package com.nc.subtopics.xa.h2.config;

import java.util.HashMap;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.arjuna.ats.internal.jta.recovery.arjunacore.XARecoveryModule;

import me.snowdrop.boot.narayana.core.jdbc.PooledXADataSourceWrapper;

@Import(NarayanaCustom.class)
@Configuration
public class XADatasourceConfig {

	@Bean(name = "internal.ds")
	public DataSource firstDataSource(TransactionManager transactionManager, XARecoveryModule xaRecoveryModule) throws Exception {
		var h2XaDataSource = new JdbcDataSource();
		h2XaDataSource.setURL("jdbc:h2:mem:internal;DB_CLOSE_DELAY=-1");

		var wrapper = new PooledXADataSourceWrapper(transactionManager, xaRecoveryModule, new HashMap<>(0));
		return wrapper.wrapDataSource(h2XaDataSource);
	}

	@Bean(name = "audit.ds")
	public DataSource secondDataSource(TransactionManager transactionManager, XARecoveryModule xaRecoveryModule) throws Exception {
		var h2XaDataSource = new JdbcDataSource();
		h2XaDataSource.setURL("jdbc:h2:mem:audit;DB_CLOSE_DELAY=-1");

		var wrapper = new PooledXADataSourceWrapper(transactionManager, xaRecoveryModule, new HashMap<>(0));
		return wrapper.wrapDataSource(h2XaDataSource);
	}

}
