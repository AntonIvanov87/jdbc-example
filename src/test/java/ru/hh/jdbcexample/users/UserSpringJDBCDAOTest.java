package ru.hh.jdbcexample.users;

public class UserSpringJDBCDAOTest extends UserDAOTestBase {

  private static final UserSpringJDBCDAO userSpringJDBCDAO = new UserSpringJDBCDAO(database);

  @Override
  protected UserDAO userDAO() {
    return userSpringJDBCDAO;
  }
}
