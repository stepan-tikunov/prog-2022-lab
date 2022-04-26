#!/bin/bash

cd "$(dirname "$0")" &&
javadoc -sourcepath src -d docs -cp $(cat .classpath) -subpackages edu.ifmo.tikunov.lab4 ;