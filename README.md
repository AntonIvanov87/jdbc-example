# JDBC EXAMPLE (PURE AND SPRING)

There are 2 branches:
* **simple** focuses on most common jdbc usage patterns, places that can be improved are commented
* **improved** contains possible improvements over simple example

Start with [UserDAO interface](src/main/java/ru/hh/jdbcexample/users/UserDAO.java).

There are 2 implementations of UserDAO:
* [UserPureJDBCDAO](src/main/java/ru/hh/jdbcexample/users/UserPureJDBCDAO.java)
* [UserSpringJDBCDAO](src/main/java/ru/hh/jdbcexample/users/UserSpringJDBCDAO.java)

See:
* [UserDAOTest](src/test/java/ru/hh/jdbcexample/users/UserDAOTestBase.java) to find how to test UserDAO implementations using in-memory database
* [Main class](src/main/java/ru/hh/jdbcexample/Main.java)

To run Main class you need Postgresql installed.
After installation run [prepare-db-as-postgres.sh](src/main/sh/prepare-db-as-postgres.sh) to create tables and users.
