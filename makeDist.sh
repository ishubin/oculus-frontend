set -e

versionInPom=`cat pom.xml | grep "<version>.*</version>" | head -n 1 | sed 's/[<version> | </version>]//g'`
version=`cat pom.xml | grep "<version>.*</version>" | head -n 1 | sed 's/[<version> | </version> | (SNAPSHOT) | -]//g'`
echo Version is $version

echo "Dumping database..."
export OCULUS_DATABASE_HOST=172.16.23.131
cd scripts
./dump.sh

cd ..

mkdir -p dist
rm -rf dist/*
mkdir -p dist/bin
mkdir -p dist/sources

cp scripts/oculus-db-init.sql dist/bin/.
cp scripts/install.sh dist/bin/.

mkdir -p target
rm -r target/*
mvn assembly:assembly -DskipTests=true

if [[ ! -f target/oculus-frontend-jar-with-dependencies.jar ]]; then
   echo "\033[31mCouldn't assembly oculus fronted library\033[00m" 
   exit 1
fi

cp target/oculus-frontend-jar-with-dependencies.jar  dist/bin/oculus-frontend.jar
cp _oculus.properties dist/bin/oculus.properties
cp log4j.properties dist/bin/log4j.properties
cp scripts/run.sh dist/bin/run.sh
cp LICENSE dist/bin/.
cp README dist/bin/.
cp -r webapp dist/bin/webapp

cp pom.xml dist/sources/.
cp LICENSE dist/sources/.
cp README dist/sources/.
cp -r src dist/sources/src
cp -r webapp dist/sources/webapp
cp scripts/oculus-db-init.sql dist/sources/.

cd dist/bin
zip -r -9 ../oculus-frontend-$version-bin.zip *

cd ../sources/
zip -r -9 ../oculus-frontend-$version-sources.zip *

cd ..
rm -rf bin/ sources/
echo Oculus Fronted project was successfully assembled
