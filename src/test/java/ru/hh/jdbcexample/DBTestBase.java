package ru.hh.jdbcexample;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public abstract class DBTestBase {

  protected static EmbeddedDatabase database;

  private static JdbcTemplate jdbcTemplate;

  @BeforeClass
  public static void setUpDBTestBaseClass() throws Exception {
    database = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("create-tables.sql")
            .build();
    jdbcTemplate = new JdbcTemplate(database);
  }

  @After
  public void tearDownDBTestBase() throws Exception {
    jdbcTemplate.update("DELETE FROM users");
  }

  @AfterClass
  public static void tearDownDBTestBaseClass() throws Exception {
    database.shutdown();
  }
}
