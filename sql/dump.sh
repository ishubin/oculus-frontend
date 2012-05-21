set -e

mysqldump -h172.16.23.131 -uroot -proot123 --opt --no-data --routines oculus > oculus-db-init.sql

#echo "insert into users (name, login, password, email, permissions) values ('admin', 'admin', 'admin', 'no-email@localhost, 'fffffffffff');" >> oculus-db-init.sql

