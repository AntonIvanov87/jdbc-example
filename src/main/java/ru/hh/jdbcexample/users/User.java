package ru.hh.jdbcexample.users;

import java.util.Objects;

public class User {

  private Integer id;  // problem: id is null for new user and not null for existing user
                       // seems that NewUser and PersistedUser are distinct classes
                       // or you can generate id outside database, in order not to rely on database sequence,
                       // which is good for distributed systems

  private String firstName;
  private String lastName;

  // factory method to create new user
  // can be constructor, but factory method has name that helps to understand its purpose
  public static User create(String firstName, String lastName) {
    return new User(null, firstName, lastName);
  }

  // factory method to load user from db
  // only UserDAO in the same package should use it, that is why it case package private visibility
  // id parameter is int - not Integer - existing user should always have id
  static User existing(int id, String firstName, String lastName) {
    return new User(id, firstName, lastName);
  }

  // private constructor, only factory methods can use it
  private User(Integer id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Integer getId() {
    return id;
  }

  // setter is package private - not public - to prevent changing id from outside
  // also id parameter is int, not Integer to prevent setting null
  void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;

    User thatUser = (User) that;
    return Objects.equals(id, thatUser.id)
            && Objects.equals(firstName, thatUser.firstName)
            && Objects.equals(lastName, thatUser.lastName);
  }

  @Override
  public int hashCode() {
    // all new users will have the same hashCode, which might lead to poor Map and Set performance
    // on the other side this hashCode implementation is super fast
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return String.format(
            "%s{id=%d, firstName='%s', lastName='%s'}",
            getClass().getSimpleName(), id, firstName, lastName
    );
  }
}
