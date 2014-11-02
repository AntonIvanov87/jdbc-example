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

    final User user1 = User.create("Ville", "Valo");
    final User user2 = User.create("Martin", "Odersky");

    userDAO().insert(user1);
    userDAO().insert(user2);

    assertEquals(user1, userDAO().get(user1.getId()).get());
    assertEquals(user2, userDAO().get(user2.getId()).get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void insertShouldThrowIllegalArgumentExceptionIfUserHasId() throws Exception {

    final User user = User.existing(1, "first name", "last name");

    userDAO().insert(user);
  }

  @Test
  public void getShouldReturnUser() throws Exception {

    final User user = User.create("Ville", "Valo");
    userDAO().insert(user);

    final Optional<User> userFromDB = userDAO().get(user.getId());

    assertEquals(user, userFromDB.get());
  }

  @Test
  public void getShouldReturnEmptyOptionalIfNoUserWithSuchId() throws Exception {

    final int nonExistentUserId = 666;

    final Optional<User> userFromDB = userDAO().get(nonExistentUserId);

    assertFalse(userFromDB.isPresent());
  }

  @Test
  public void getAllShouldReturnAllUsers() throws Exception {

    assertTrue(userDAO().getAll().isEmpty());

    final User user1 = User.create("Joe", "Armstrong");
    final User user2 = User.create("Martin", "Odersky");

    userDAO().insert(user1);
    userDAO().insert(user2);

    final Set<User> usersFromDB = userDAO().getAll();

    assertEquals(new HashSet<>(Arrays.asList(user1, user2)), usersFromDB);
  }

  @Test
  public void updateShouldUpdateUser() throws Exception {

    final User user = User.create("Ville", "Valo");
    userDAO().insert(user);
    user.setFirstName("Ivan");

    userDAO().update(user);

    final User userFromDB = userDAO().get(user.getId()).get();
    assertEquals(user, userFromDB);
  }

  @Test
  public void deleteShouldDeleteUserById() throws Exception {

    final User user1 = User.create("Joe", "Armstrong");
    final User user2 = User.create("Martin", "Odersky");

    userDAO().insert(user1);
    userDAO().insert(user2);

    userDAO().delete(user1.getId());

    assertFalse(userDAO().get(user1.getId()).isPresent());
    assertTrue(userDAO().get(user2.getId()).isPresent());
  }
}
