#!/usr/bin/env bash


mvn archetype:create-from-project
mvn -f target/generated-sources/archetype install