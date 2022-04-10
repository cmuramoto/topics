package com.nc.topics.quarkus.xa.test.spring;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.nc.topics.quarkus.xa.profiles.H2_MySql_Profile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(H2_MySql_Profile.class)
public class H2_MySQL_XAIntegrationTest extends XATestTemplate {

	@Inject
	@Named("movie")
	DataSource movieds;

	@Inject
	@Named("audit")
	DataSource auditds;

	@Test
	@Order(Integer.MIN_VALUE)
	public void cleanup() throws SQLException {
		purge(movieds, "Movie");
		purge(auditds, "AuditAction");
	}

	@Transactional
	public void purge(DataSource ds, String table) throws SQLException {
		log.infof("Deleting records from {}", table);

		try (var conn = ds.getConnection(); var pst = conn.prepareStatement("DELETE FROM " + table)) {
			var deleted = pst.executeUpdate();

			log.infof("Deleted {} records from {}", deleted, table);
		}
	}
}