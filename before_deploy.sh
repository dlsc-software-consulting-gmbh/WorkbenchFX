#!/usr/bin/env bash
mvn -pl "!workbenchfx-dirk" install -DskipTests
zip -r ${TRAVIS_BUILD_DIR}/javadoc.zip ${TRAVIS_BUILD_DIR}/workbenchfx-core/target/site
zip -r ${TRAVIS_BUILD_DIR}/documentation.zip ${TRAVIS_BUILD_DIR}/target/generated-docs
