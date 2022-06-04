#!/bin/bash

cd "$(dirname "$0")" ;

rm -rf bin/* 2>/dev/null ;
cp lib/*.jar bin/ ;
mkdir bin 2>/dev/null ;
javac -source 1.8 -target 1.8 -encoding "UTF-8" -s src -d bin -cp $(cat .classpath) $(find src -name "*.java") &&
cd bin &&
find ./ -name "*.jar" -exec jar -xf {} \; &&
jar cfm server.jar ../SERVER.mf ../lib/logback.xml $(find . -name "*.class" -not -path "./edu/ifmo/tikunov/lab5/client/*") &&
jar cfm client.jar ../CLIENT.mf $(find . -name "*.class" -not -path "./edu/ifmo/tikunov/lab5/server/*") &&
echo "Compiled successfully, server.jar, client.jar and *.class files are stored in bin/ directory." ;
