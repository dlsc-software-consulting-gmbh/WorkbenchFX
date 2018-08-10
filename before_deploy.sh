#!/usr/bin/env bash
echo "Running mvn install"
mvn install -DskipTests
echo "Making zips"
cd ${TRAVIS_BUILD_DIR}/workbenchfx-core/target/apidocs
zip -r ${TRAVIS_BUILD_DIR}/javadoc.zip .
cd ${TRAVIS_BUILD_DIR}/target/generated-docs
zip -r ${TRAVIS_BUILD_DIR}/documentation.zip .
