package ru.hh.jdbcexample.users;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class UserPureJDBCDAO implements UserDAO {

  private final DataSource dataSource;

  public UserPureJDBCDAO(DataSource dataSource) {
    this.dataSource = requireNonNull(dataSource);
  }

  @Override
  public void insert(User user) {

    if (user.getId() != null) {
      // problem: runtime exception, can we move to compile time?
      throw new IllegalArgumentException("can not save " + user + " with already assigned id");
    }

    try (Connection connection = dataSource.getConnection()) {

      String query = "INSERT INTO users (first_name, last_name) VALUES (?, ?)";
      try (PreparedStatement statement =
               connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

        statement.setString(1, user.getFirstName());  // problem: positional arguments
        statement.setString(2, user.getLastName());

        statement.executeUpdate();

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
          generatedKeys.next();
          user.setId(generatedKeys.getInt(1));  // problem: what if user is already in some set?
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException("failed to persist " + user, e);
    }
  }

  @Override
  public Optional<User> get(int userId) {

    try (Connection connection = dataSource.getConnection()) {

      String query = "SELECT user_id, first_name, last_name FROM users WHERE user_id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, userId);

        try (ResultSet resultSet = statement.executeQuery()) {

          boolean userExists = resultSet.next();
          if (!userExists) {
            return Optional.empty();
          }
          return Optional.of(
                  User.existing(
                          userId,
                          resultSet.getString("first_name"),
                          resultSet.getString("last_name")
                  )
          );
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("failed to get user by id " + userId, e);
    }
  }

  @Override
  public Set<User> getAll() {

    try (Connection connection = dataSource.getConnection()) {

      try (Statement statement = connection.createStatement()) {

        String query = "SELECT user_id, first_name, last_name FROM users";
        try (ResultSet resultSet = statement.executeQuery(query)) {

          Set<User> users = new HashSet<>();
          while (resultSet.next()) {
            User user = User.existing(
                resultSet.getInt("user_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
            );
            users.add(user);
          }
          return users;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("failed to get users", e);
    }
  }

  @Override
  public void update(User user) {

    if (user.getId() == null) {
      throw new IllegalArgumentException("can not update " + user + " without id");
    }

    try(Connection connection = dataSource.getConnection()) {

      String query = "UPDATE users SET first_name = ?, last_name = ? WHERE user_id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setInt(3, user.getId());

        statement.executeUpdate();
      }

    } catch (SQLException e) {
      throw new RuntimeException("failed to update " + user, e);
    }
  }

  @Override
  public void delete(int userId) {

    try (Connection connection = dataSource.getConnection()) {

      String query = "DELETE FROM users WHERE user_id = ?";
      try(PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, userId);

        statement.executeUpdate();
      }

    } catch (SQLException e) {
      throw new RuntimeException("failed to remove user by id " + userId, e);
    }
  }
}
