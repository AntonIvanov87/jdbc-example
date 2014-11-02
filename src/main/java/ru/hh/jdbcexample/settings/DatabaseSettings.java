package ru.hh.jdbcexample.settings;

import static java.util.Objects.requireNonNull;

public class DatabaseSettings {

  public final String url;
  public final String user;
  public final String password;

  DatabaseSettings(final String url, final String user, final String password) {
    this.url = requireNonNull(url);
    this.user = requireNonNull(user);
    this.password = requireNonNull(password);
  }
}
