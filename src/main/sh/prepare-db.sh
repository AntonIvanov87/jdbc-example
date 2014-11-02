#!/bin/sh
createdb jdbc_example
createuser -P jdbc_example
psql -U jdbc_example -h localhost < ../resources/create-tables.sql
