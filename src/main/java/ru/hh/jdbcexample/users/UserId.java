package ru.hh.jdbcexample.users;

public class UserId {

  public final int val;

  public UserId(final int val) {
    this.val = val;
  }

  @Override
  public boolean equals(final Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;

    final UserId thatUserId = (UserId) that;
    return val == thatUserId.val;
  }

  @Override
  public int hashCode() {
    return val;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + '{' + val + '}';
  }
}
