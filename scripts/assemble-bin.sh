set -e

echo "Dumping database..."

export OCULUS_DATABASE_HOST=172.16.23.131
./dump.sh

rm -rf ../bin/*

cp oculus-db-init.sql ../bin/.
cp install.sh ../bin/.

cd ..
rm -r target/*
mvn assembly:assembly -DskipTests=true

if [[ ! -f target/oculus-frontend-jar-with-dependencies.jar ]]; then
   echo "\033[31mCouldn't assembly oculus fronted library\033[00m" 
   exit 1
fi

cp target/oculus-frontend-jar-with-dependencies.jar  bin/oculus-frontend.jar
cp oculus.properties bin/oculus.properties
cp log4j.properties bin/log4j.properties
cp scripts/run.sh bin/run.sh

echo Oculus Fronted project was successfully assembled
