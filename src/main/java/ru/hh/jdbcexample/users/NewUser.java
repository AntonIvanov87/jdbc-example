package ru.hh.jdbcexample.users;

public class NewUser extends AbstractUser {

  public NewUser(final String firstName, final String lastName) {
    super(firstName, lastName);
  }

  @Override
  public String toString() {
    return String.format("%s{%s}", getClass().getSimpleName(), super.fieldsToString());
  }
}
