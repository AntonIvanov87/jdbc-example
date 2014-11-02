package ru.hh.jdbcexample.users;

import static java.util.Objects.requireNonNull;

public class User extends AbstractUser {

  public final UserId id;  // warn: separate class for id field is not common practice

  User(final UserId id, final String firstName, final String lastName) {
    super(firstName, lastName);
    this.id = requireNonNull(id);
  }

  // fluent interface ready :-)
  public User withFirstName(final String firstName) {
    return new User(id, firstName, super.lastName);
  }

  @Override
  public boolean equals(final Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;
    if (!super.equals(that)) return false;

    final User thatUser = (User) that;
    return id.equals(thatUser.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return String.format(
            "%s{id=%d, %s}",
            getClass().getSimpleName(), id.val, super.fieldsToString()
    );
  }
}
