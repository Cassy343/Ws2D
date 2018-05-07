#!/bin/bash

SOURCE_DIR="./src"
BUILD_DIR="./build"
DIST_DIR="./run"
mkdir -p $BUILD_DIR

find $SOURCE_DIR -name "*.java" > sources.txt
javac -cp @classpath.txt @sources.txt -d $BUILD_DIR
cp -a $SOURCE_DIR/. $BUILD_DIR
find $BUILD_DIR -name "*.java" -delete
rm sources.txt

mkdir -p $DIST_DIR
jar cfm $DIST_DIR/game.jar META-INF/manifest.mf -C $BUILD_DIR .