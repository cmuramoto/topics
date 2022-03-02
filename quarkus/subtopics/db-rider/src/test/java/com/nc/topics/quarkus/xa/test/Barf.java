package com.nc.topics.quarkus.xa.test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.IntStream;

public class Barf {

	public static void main(String[] args) throws SQLException {
		var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "pass");

		var md = connection.getMetaData();

		var tables = md.getTables(null, null, "%tb_movie%", null);

		if (tables.next()) {
			do {
				var rmd = tables.getMetaData();

				var cols = rmd.getColumnCount();
				var names = IntStream.rangeClosed(1, cols).mapToObj(v -> {
					try {
						return rmd.getColumnName(v);
					} catch (SQLException e) {
						return "";
					}
				}).toList();

				System.out.println(tables.getString("TABLE_NAME"));
			} while (tables.next());
		}
	}

}
