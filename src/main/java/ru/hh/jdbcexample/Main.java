package ru.hh.jdbcexample;

import ru.hh.jdbcexample.users.User;
import ru.hh.jdbcexample.users.UserDAO;
import ru.hh.jdbcexample.users.UserPureJDBCDAO;
import ru.hh.jdbcexample.users.UserSpringJDBCDAO;
import ru.hh.jdbcexample.utils.PropertiesFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static ru.hh.jdbcexample.utils.DataSourceFactory.createPGSimpleDataSource;

class Main {

  public static void main(final String... args) throws IOException {

    final Properties properties = PropertiesFactory.load();

    final DataSource dataSource = createPGSimpleDataSource(
            properties.getProperty("jdbc.url"),
            properties.getProperty("jdbc.user"),
            properties.getProperty("jdbc.password")
    );

    System.out.println("PURE JDBC EXAMPLE");
    System.out.println();
    play(new UserPureJDBCDAO(dataSource));

    System.out.println("SPRING JDBC EXAMPLE");
    System.out.println();
    play(new UserSpringJDBCDAO(dataSource));
  }

  private static void play(final UserDAO userDAO) {

    final User user = User.create("Gordon", "Freeman");
    userDAO.insert(user);
    System.out.println("persisted " + user);
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    user.setFirstName("Morgan");
    userDAO.update(user);
    System.out.println("updated Gordon to Morgan");
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    userDAO.delete(user.getId());
    System.out.println("deleted user with id " + user.getId());
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    final Optional<User> emptyMorganFreeman = userDAO.get(user.getId());
    System.out.println("tried to get user by " + user.getId() + " but got " + emptyMorganFreeman);
    System.out.println();
  }

  private Main() {
  }
}
