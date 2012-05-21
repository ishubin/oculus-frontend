set -e

function readVar {
    printf "$1"
    if [[ -n $3 ]]; then
        printf " [$3]: "
    else
        printf ": "
    fi
    read input

    if [[ -n $3 && -z $input ]]; then 
        input=$3
    fi

    eval "$2=\$input"
}

echo "This script will initialize database for Oculus Frontend"

readVar "Enter mysql host" host localhost

readVar "Enter mysql user" user root

readVar "Enter mysql password for '$user'" pass

readVar "Enter scheme name" scheme oculus

mysql -h$host -u$user -p$pass -e "create database if not exists $scheme"

mysql --verbose --host=$host --user=$user --password=$pass $scheme < oculus-db-init.sql
