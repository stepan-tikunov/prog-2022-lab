#!/bin/bash

cd "$(dirname "$0")" &&
javadoc -sourcepath src -d doc -cp $(cat .classpath) -subpackages edu.ifmo.tikunov.lab4 ;