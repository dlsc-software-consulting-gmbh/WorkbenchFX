#!/usr/bin/env bash
echo "Running mvn install"
mvn -pl "!workbenchfx-dirk" install -DskipTests
echo "Making zips"
zip -r ${TRAVIS_BUILD_DIR}/javadoc.zip ${TRAVIS_BUILD_DIR}/workbenchfx-core/target/site
zip -r ${TRAVIS_BUILD_DIR}/documentation.zip ${TRAVIS_BUILD_DIR}/target/generated-docs
