package ru.hh.jdbcexample.users;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UserSpringJDBCDAO implements UserDAO {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  public UserSpringJDBCDAO(DataSource dataSource) {

    jdbcTemplate = new JdbcTemplate(dataSource);
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("users")
            .usingGeneratedKeyColumns("user_id");
  }

  @Override
  public void insert(User user) {

    if (user.getId() != null) {
      throw new IllegalArgumentException("can not insert " + user + " with already assigned id");
    }

    Map<String, Object> params = new HashMap<>();
    params.put("first_name", user.getFirstName());
    params.put("last_name", user.getLastName());

    int userId = simpleJdbcInsert.executeAndReturnKey(params).intValue();

    user.setId(userId);
  }

  @Override
  public Optional<User> get(int userId) {

    String query = "SELECT user_id, first_name, last_name FROM users WHERE user_id = :user_id";

    Map<String, Object> params = new HashMap<>();
    params.put("user_id", userId);

    User user;
    try {
      user = namedParameterJdbcTemplate.queryForObject(query, params, rowToUser);
    } catch (EmptyResultDataAccessException ignored) {
      return Optional.empty();
    }
    return Optional.of(user);
  }

  @Override
  public Set<User> getAll() {

    String query = "SELECT user_id, first_name, last_name FROM users";

    return new HashSet<>(jdbcTemplate.query(query, rowToUser));
  }

  @Override
  public void update(User user) {

    if (user.getId() == null) {
      throw new IllegalArgumentException("can not update " + user + " without id");
    }

    String query = "UPDATE users SET first_name = :first_name, last_name = :last_name WHERE user_id = :user_id";

    Map<String, Object> params = new HashMap<>();
    params.put("first_name", user.getFirstName());
    params.put("last_name", user.getLastName());
    params.put("user_id", user.getId());

    namedParameterJdbcTemplate.update(query, params);
  }

  @Override
  public void delete(int userId) {

    String query = "DELETE FROM users WHERE user_id = :user_id";

    Map<String, Object> params = new HashMap<>();
    params.put("user_id", userId);

    namedParameterJdbcTemplate.update(query, params);
  }

  private static final RowMapper<User> rowToUser = (resultSet, rowNum) ->
          User.existing(
                  resultSet.getInt("user_id"),
                  resultSet.getString("first_name"),
                  resultSet.getString("last_name")
          );
}
