package ru.hh.jdbcexample.users;

public class UserPureJDBCDAOTest extends UserDAOTestBase {

  private static final UserPureJDBCDAO userPureJDBCDAO = new UserPureJDBCDAO(database);

  @Override
  protected UserDAO userDAO() {
    return userPureJDBCDAO;
  }
}
