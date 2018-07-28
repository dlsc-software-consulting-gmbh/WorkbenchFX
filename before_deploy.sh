#!/usr/bin/env bash
echo "Running mvn install"
mvn -pl "!workbenchfx-dirk" install -DskipTests
echo "Making zips"
cd ${TRAVIS_BUILD_DIR}/workbenchfx-core/target/site/apidocs
zip -r ${TRAVIS_BUILD_DIR}/javadoc.zip .
cd ${TRAVIS_BUILD_DIR}/target/generated-docs
zip -r ${TRAVIS_BUILD_DIR}/documentation.zip .
