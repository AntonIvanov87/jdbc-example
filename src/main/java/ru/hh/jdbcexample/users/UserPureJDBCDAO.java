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

  public UserPureJDBCDAO(final DataSource dataSource) {
    this.dataSource = requireNonNull(dataSource);
  }

  @Override
  public User insert(final NewUser newUser) {

    // yey, no runtime exception!

    try (final Connection connection = dataSource.getConnection()) {

      final String query = "INSERT INTO users (first_name, last_name) VALUES (?, ?)";
      try (final PreparedStatement statement =
                   connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

        statement.setString(1, newUser.firstName);
        statement.setString(2, newUser.lastName);

        statement.executeUpdate();

        try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
          generatedKeys.next();
          final UserId userId = new UserId(generatedKeys.getInt(1));
          return new User(userId, newUser.firstName, newUser.lastName);
        }
      }

    } catch (final SQLException e) {
      throw new RuntimeException("failed to persist " + newUser, e);
    }
  }

  @Override
  public Optional<User> get(final UserId userId) {

    try (final Connection connection = dataSource.getConnection()) {

      final String query = "SELECT user_id, first_name, last_name FROM users WHERE user_id = ?";
      try (final PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, userId.val);

        try (final ResultSet resultSet = statement.executeQuery()) {

          final boolean userExists = resultSet.next();
          if (!userExists) {
            return Optional.empty();
          }
          return Optional.of(
                  new User(
                          userId,
                          resultSet.getString("first_name"),
                          resultSet.getString("last_name")
                  )
          );
        }
      }
    } catch (final SQLException e) {
      throw new RuntimeException("failed to get user by " + userId, e);
    }
  }

  @Override
  public Set<User> getAll() {

    try (final Connection connection = dataSource.getConnection()) {

      try (final Statement statement = connection.createStatement()) {

        final String query = "SELECT user_id, first_name, last_name FROM users";
        try (final ResultSet resultSet = statement.executeQuery(query)) {

          final Set<User> users = new HashSet<>();
          while (resultSet.next()) {
            final UserId userId = new UserId(resultSet.getInt("user_id"));
            final User user = new User(
                    userId,
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name")
            );
            users.add(user);
          }
          return users;
        }
      }
    } catch (final SQLException e) {
      throw new RuntimeException("failed to get users", e);
    }
  }

  @Override
  public void update(final User user) {

    // no more runtime exception

    try(final Connection connection = dataSource.getConnection()) {

      final String query = "UPDATE users SET first_name = ?, last_name = ? WHERE user_id = ?";
      try (final PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, user.firstName);
        statement.setString(2, user.lastName);
        statement.setInt(3, user.id.val);

        statement.executeUpdate();
      }

    } catch (final SQLException e) {
      throw new RuntimeException("failed to update " + user, e);
    }
  }

  @Override
  public void delete(final UserId userId) {

    try (final Connection connection = dataSource.getConnection()) {

      final String query = "DELETE FROM users WHERE user_id = ?";
      try(final PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, userId.val);

        statement.executeUpdate();
      }

    } catch (final SQLException e) {
      throw new RuntimeException("failed to remove user by " + userId, e);
    }
  }
}
