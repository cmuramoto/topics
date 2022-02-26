package com.nc.subtopics.xa.h2.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.nc.domain.internal.AppUser;
import com.nc.domain.internal.Match;
import com.nc.domain.internal.Movie;
import com.nc.domain.internal.QuizzRanking;
import com.nc.domain.internal.Round;

@Configuration
@EnableJpaRepositories(basePackages = "com.nc.repositories.jpa.internal", entityManagerFactoryRef = "internal.em")
public class PersistenceInternalConfig {

	@Autowired
	@Qualifier("audit.ds")
	DataSource ds;

	final Logger log = LoggerFactory.getLogger(getClass());

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

		factory.setPersistenceUnitName("internal.pu");
		factory.setPersistenceUnitPostProcessors(pu -> {
			pu.addManagedClassName(AppUser.class.getName());
			pu.addManagedClassName(Match.class.getName());
			pu.addManagedClassName(Movie.class.getName());
			pu.addManagedClassName(QuizzRanking.class.getName());
			pu.addManagedClassName(Round.class.getName());
		});

		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setJpaProperties(props);
		factory.setJtaDataSource(ds);

		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean(name = "internal.em")
	public EntityManagerFactory em() {
		return byFactory();
	}

}
