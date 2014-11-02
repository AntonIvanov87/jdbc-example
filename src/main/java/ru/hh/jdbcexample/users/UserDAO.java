package ru.hh.jdbcexample.users;

import java.util.Optional;
import java.util.Set;

public interface UserDAO {

  User insert(NewUser newUser);

  // do not return null, use Optional
  Optional<User> get(UserId userId);  // now we can not pass employerId

  Set<User> getAll();

  void update(User user);

  // it is better not to delete entities, mark them as disabled instead
  void delete(UserId userId);  // now we can not pass employerId
}
