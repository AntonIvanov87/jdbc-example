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

    final NewUser newUser1 = new NewUser("Ville", "Valo");
    final NewUser newUser2 = new NewUser("Martin", "Odersky");

    final User user1 = userDAO().insert(newUser1);
    final User user2 = userDAO().insert(newUser2);

    assertEquals(newUser1.firstName, user1.firstName);
    assertEquals(newUser1.lastName, user1.lastName);
    assertEquals(user1, userDAO().get(user1.id).get());

    assertEquals(newUser2.firstName, user2.firstName);
    assertEquals(newUser2.lastName, user2.lastName);
    assertEquals(user2, userDAO().get(user2.id).get());
  }

  @Test
  public void getShouldReturnUser() throws Exception {

    final User user = userDAO().insert(new NewUser("Ville", "Valo"));

    final Optional<User> userFromDB = userDAO().get(user.id);

    assertEquals(user, userFromDB.get());
  }

  @Test
  public void getShouldReturnEmptyOptionalIfNoUserWithSuchId() throws Exception {

    final UserId nonExistentUserId = new UserId(666);

    final Optional<User> userFromDB = userDAO().get(nonExistentUserId);

    assertFalse(userFromDB.isPresent());
  }

  @Test
  public void getAllShouldReturnAllUsers() throws Exception {

    assertTrue(userDAO().getAll().isEmpty());

    final User user1 = userDAO().insert(new NewUser("Joe", "Armstrong"));
    final User user2 = userDAO().insert(new NewUser("Martin", "Odersky"));

    final Set<User> usersFromDB = userDAO().getAll();

    assertEquals(new HashSet<>(Arrays.asList(user1, user2)), usersFromDB);
  }

  @Test
  public void updateShouldUpdateUser() throws Exception {

    final NewUser newUser = new NewUser("Ville", "Valo");
    final User user = userDAO().insert(newUser);
    final User updatedUser = user.withFirstName("Ivan");

    userDAO().update(updatedUser);

    final User updatedUserFromDB = userDAO().get(user.id).get();
    assertEquals(updatedUser, updatedUserFromDB);
  }

  @Test
  public void deleteShouldDeleteUserById() throws Exception {

    final User user1 = userDAO().insert(new NewUser("Ville", "Valo"));
    final User user2 = userDAO().insert(new NewUser("Martin", "Odersky"));

    userDAO().delete(user1.id);

    assertFalse(userDAO().get(user1.id).isPresent());
    assertTrue(userDAO().get(user2.id).isPresent());
  }
}
