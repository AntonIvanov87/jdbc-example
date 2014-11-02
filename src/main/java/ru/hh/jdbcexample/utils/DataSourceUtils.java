package ru.hh.jdbcexample.utils;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.SQLException;

public final class DataSourceUtils {

  public static PGSimpleDataSource pgSimpleDataSource(final String dbUrl, final String dbUser, final String dbPassword)
          throws SQLException {

    final PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
    pgSimpleDataSource.setUrl(dbUrl);
    pgSimpleDataSource.setUser(dbUser);
    pgSimpleDataSource.setPassword(dbPassword);
    return pgSimpleDataSource;
  }

  private DataSourceUtils() {
  }
}
