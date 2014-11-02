package ru.hh.jdbcexample.users;

import org.junit.Test;
import ru.hh.jdbcexample.DBTestBase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class UserDAOTestBase extends DBTestBase {

  protected abstract UserDAO userDAO();

  @Test
  public void insertShouldInsertNewUserInDBAndReturnUserWithAssignedId() throws Exception {

    User user1 = User.create("Ville", "Valo");
    User user2 = User.create("Martin", "Odersky");

    userDAO().insert(user1);
    userDAO().insert(user2);

    User user1FromDB = userDAO().get(user1.getId()).get();
    assertEquals(user1, user1FromDB);

    User user2FromDB = userDAO().get(user2.getId()).get();
    assertEquals(user2, user2FromDB);
  }

  @Test(expected = IllegalArgumentException.class)
  public void insertShouldThrowIllegalArgumentExceptionIfUserHasId() throws Exception {

    User user = User.existing(1, "first name", "last name");

    userDAO().insert(user);
  }

  @Test
  public void getShouldReturnUser() throws Exception {

    User user = User.create("Ville", "Valo");
    userDAO().insert(user);

    Optional<User> userFromDB = userDAO().get(user.getId());

    assertEquals(user, userFromDB.get());
  }

  @Test
  public void getShouldReturnEmptyOptionalIfNoUserWithSuchId() throws Exception {

    int nonExistentUserId = 666;

    Optional<User> userFromDB = userDAO().get(nonExistentUserId);

    assertFalse(userFromDB.isPresent());
  }

  @Test
  public void getAllShouldReturnAllUsers() throws Exception {

    assertTrue(userDAO().getAll().isEmpty());

    User user1 = User.create("Joe", "Armstrong");
    User user2 = User.create("Martin", "Odersky");

    userDAO().insert(user1);
    userDAO().insert(user2);

    Set<User> usersFromDB = userDAO().getAll();

    assertEquals(new HashSet<>(Arrays.asList(user1, user2)), usersFromDB);
  }

  @Test
  public void updateShouldUpdateUser() throws Exception {

    User user = User.create("Ville", "Valo");
    userDAO().insert(user);
    user.setFirstName("Ivan");

    userDAO().update(user);

    User userFromDB = userDAO().get(user.getId()).get();
    assertEquals(user, userFromDB);
  }

  @Test
  public void deleteShouldDeleteUserById() throws Exception {

    User user1 = User.create("Joe", "Armstrong");
    User user2 = User.create("Martin", "Odersky");

    userDAO().insert(user1);
    userDAO().insert(user2);

    userDAO().delete(user1.getId());

    assertFalse(userDAO().get(user1.getId()).isPresent());
    assertTrue(userDAO().get(user2.getId()).isPresent());
  }
}
