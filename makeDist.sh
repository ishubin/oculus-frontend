set -e

echo "Dumping database..."

export OCULUS_DATABASE_HOST=172.16.23.131
cd scripts
./dump.sh

mkdir -p ../bin
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
cp _oculus.properties bin/oculus.properties
cp log4j.properties bin/log4j.properties
cp scripts/run.sh bin/run.sh
cp -r webapp bin/webapp
cd bin
zip -r -9 oculus-frontend.zip *
ls -1 | grep -v "oculus-frontend.zip" | xargs rm -rf

echo Oculus Fronted project was successfully assembled
