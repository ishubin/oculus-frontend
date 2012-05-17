#!/bin/sh
set -e

mysqldump -h172.16.23.131 -uroot -proot123 --opt --no-data --routines oculus > temp.sql

echo "\ninsert into users (name, login, password, email, permissions) values ('admin', 'admin', 'admin', 'no-email@localhost, 'fffffffffff');" >> temp.sql

sed '/^--.*$/d' temp.sql | sed '/^$/d' > oculus-db-init.sql

rm temp.sql
