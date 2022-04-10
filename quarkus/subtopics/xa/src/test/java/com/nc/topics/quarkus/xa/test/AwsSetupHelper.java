package com.nc.topics.quarkus.xa.test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class AwsSetupHelper {

	public static void main(String[] args) throws SQLException {
		try (var conn = DriverManager.getConnection("jdbc:mysql://database-1.caikdtqeucyk.sa-east-1.rds.amazonaws.com:3306", "admin", "password")) {
			var dbmd = conn.getMetaData();
			var dbPresent = false;

			try (var catalogs = dbmd.getCatalogs()) {
				while (catalogs.next()) {
					var db = catalogs.getString(1);
					if ("auditdb".equals(db)) {
						dbPresent = true;
						break;
					}
				}
			}

			if (!dbPresent) {
				System.out.println("Creating auditdb");
				try (var ps = conn.prepareStatement("CREATE DATABASE auditdb")) {
					if (!ps.execute()) {
						System.out.println("SUX");
					}
				}
			}
		}
	}

}
