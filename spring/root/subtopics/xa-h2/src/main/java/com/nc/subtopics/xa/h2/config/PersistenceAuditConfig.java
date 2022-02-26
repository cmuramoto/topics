package com.nc.subtopics.xa.h2.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.nc.domain.audit.AuditAction;

@Configuration
@EnableJpaRepositories(basePackages = "com.nc.repositories.jpa.audit", entityManagerFactoryRef = "audit.em")
public class PersistenceAuditConfig {

	@Autowired
	@Qualifier("audit.ds")
	DataSource ds;

	private EntityManagerFactory byFactory() {
		var factory = new LocalContainerEntityManagerFactoryBean();
		// set fake packaged, otherwise it will fail
		factory.setPackagesToScan("!");

		var props = new Properties();
		props.put(AvailableSettings.DIALECT, "org.hibernate.dialect.H2Dialect");
		props.put(AvailableSettings.SHOW_SQL, "false");
		props.put(AvailableSettings.FORMAT_SQL, "true");
		props.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
		props.put(AvailableSettings.PERSISTENCE_UNIT_NAME, "audit.pu");

		factory.setPersistenceUnitName("audit.pu");
		factory.setPersistenceUnitPostProcessors(pu -> {
			pu.addManagedClassName(AuditAction.class.getName());
		});

		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setJpaProperties(props);
		factory.setJtaDataSource(ds);

		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean(name = "audit.em")
	public EntityManagerFactory em() {
		return byFactory();
		// return byBuilder();
	}
}
