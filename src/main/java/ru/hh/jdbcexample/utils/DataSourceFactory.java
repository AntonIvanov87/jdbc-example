package ru.hh.jdbcexample.utils;

import org.postgresql.ds.PGSimpleDataSource;

public class DataSourceFactory {

  public static PGSimpleDataSource createPGSimpleDataSource(String url, String user, String password) {
    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
    pgSimpleDataSource.setUrl(url);
    pgSimpleDataSource.setUser(user);
    pgSimpleDataSource.setPassword(password);
    return pgSimpleDataSource;
  }

  private DataSourceFactory() {
  }
}
