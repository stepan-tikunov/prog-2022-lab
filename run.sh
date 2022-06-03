#!/bin/bash

cd "$(dirname "$0")" ;

java -jar -Xmx128M -Xms128M "bin/$1.jar" ;
