package ru.hh.jdbcexample.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFactory {

  public static Properties load() throws IOException {
    ClassLoader classLoader = PropertiesFactory.class.getClassLoader();

    Properties defaultProperties = new Properties();
    try (InputStream defaultInputStream = classLoader.getResourceAsStream("default.properties")) {
      defaultProperties.load(defaultInputStream);
    }

    try (InputStream overriddenInputStream = classLoader.getResourceAsStream("overridden.properties")) {
      if (overriddenInputStream == null) {
        return defaultProperties;
      }
      Properties overriddenProperties = new Properties(defaultProperties);
      overriddenProperties.load(overriddenInputStream);
      return overriddenProperties;
    }
  }

  private PropertiesFactory() {
  }
}
