#!/bin/sh
set -e

if [[ -z $OCULUS_DATABASE_HOST ]]; then
    echo "\033[31mOCULUS_DATABASE_HOST variable is not specified\033[00m"
    exit 1
fi

echo "dumping database on host $OCULUS_DATABASE_HOST"

mysqldump -h$OCULUS_DATABASE_HOST -uroot -proot123 --opt --no-data --routines oculus > temp.sql

echo "insert into users (name, login, password, email, permissions) values ('admin', 'admin', 'admin', 'no-email@localhost', 'fffffffffff');" >> temp.sql

sed '/^--.*$/d' temp.sql | sed '/^$/d' > oculus-db-init.sql

rm temp.sql

echo "\033[32mDatabase was dumped successfully on host $OCULUS_DATABASE_HOST\033[00m"
