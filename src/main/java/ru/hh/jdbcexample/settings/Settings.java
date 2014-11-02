package ru.hh.jdbcexample.settings;

import static java.util.Objects.requireNonNull;

public class Settings {

  public final DatabaseSettings database;

  Settings(final DatabaseSettings database) {
    this.database = requireNonNull(database);
  }
}
