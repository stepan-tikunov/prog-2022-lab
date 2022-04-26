#!/bin/bash

cd "$(dirname "$0")" ;

java -jar bin/app.jar -Xmx128M -Xms128M ;
