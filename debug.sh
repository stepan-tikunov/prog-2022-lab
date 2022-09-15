#!/bin/sh

cd "$(dirname "$0")" ;

jdb -sourcepath src/ -classpath "$(cat .classpath)" edu.ifmo.tikunov.lab1.Main ;
