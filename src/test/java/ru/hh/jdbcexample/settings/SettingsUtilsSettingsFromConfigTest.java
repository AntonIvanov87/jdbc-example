package ru.hh.jdbcexample.settings;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.hh.jdbcexample.settings.SettingsUtils.settingsFromConfig;

public class SettingsUtilsSettingsFromConfigTest {

  @Test(expected = ConfigException.Missing.class)
  public void shouldThrowConfigExceptionMissingIfDatabaseSectionNotFound() throws Exception {

    final Config config = ConfigFactory.parseResourcesAnySyntax("missing.conf");
    assertFalse(config.hasPath("database"));

    settingsFromConfig(config);
  }

  @Test(expected = ConfigException.Missing.class)
  public void shouldThrowConfigExceptionMissingIfDatabasePasswordNotFound() throws Exception {

    final Config config = ConfigFactory.parseResourcesAnySyntax("ru/hh/jdbcexample/settings/missingDatabasePassword.conf");
    assertTrue(config.hasPath("database"));

    settingsFromConfig(config);
  }

  @Test
  public void shouldReturnSettings() throws Exception {

    final Config config = ConfigFactory.load("ru/hh/jdbcexample/settings/application.conf");

    final Settings settings = settingsFromConfig(config);

    assertEquals("some url", settings.database.url);
    assertEquals("some user", settings.database.user);
    assertEquals("some password", settings.database.password);
  }
}
