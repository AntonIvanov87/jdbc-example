package ru.hh.jdbcexample;

import ru.hh.jdbcexample.settings.Settings;
import ru.hh.jdbcexample.users.NewUser;
import ru.hh.jdbcexample.users.User;
import ru.hh.jdbcexample.users.UserDAO;
import ru.hh.jdbcexample.users.UserPureJDBCDAO;
import ru.hh.jdbcexample.users.UserSpringJDBCDAO;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

import static ru.hh.jdbcexample.settings.SettingsUtils.loadSettings;
import static ru.hh.jdbcexample.utils.DataSourceUtils.pgSimpleDataSource;

final class Main {

  public static void main(final String... args) throws SQLException {

    final Settings settings = loadSettings();

    final DataSource dataSource = pgSimpleDataSource(
            settings.database.url, settings.database.user, settings.database.password
    );

    System.out.println("Pure JDBC example");
    play(new UserPureJDBCDAO(dataSource));

    System.out.println("Spring JDBC example");
    play(new UserSpringJDBCDAO(dataSource));
  }

  private static void play(final UserDAO userDAO) {

    final NewUser newGordonFreeman = new NewUser("Gordon", "Freeman");
    final User gordonFreeman = userDAO.insert(newGordonFreeman);
    System.out.println("persisted " + newGordonFreeman);
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    final User morganFreeman = gordonFreeman.withFirstName("Morgan");
    userDAO.update(morganFreeman);
    System.out.println("updated " + gordonFreeman + " to " + morganFreeman);
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    userDAO.delete(morganFreeman.id);
    System.out.println("deleted user with " + morganFreeman.id);
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    final Optional<User> emptyMorganFreeman = userDAO.get(morganFreeman.id);
    System.out.println("tried to get user by " + morganFreeman.id + " but got " + emptyMorganFreeman);
    System.out.println();
  }

  private Main() {
  }
}
