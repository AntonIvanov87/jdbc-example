package ru.hh.jdbcexample.users;

import java.util.Optional;
import java.util.Set;

public interface UserDAO {

  void insert(User user);

  // do not return null, use Optional
  Optional<User> get(int userId);  // problem: userId is not strongly typed, what if we pass int employerId?

  Set<User> getAll();

  void update(User user);

  // it is better not to delete entities, mark them as disabled instead
  void delete(int userId); // problem: userId is not strongly typed, what if we pass int employerId?
}
