#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
bash scripts/build.sh
mkdir -p build/test-classes
find src/test/java -name '*.java' | sort > build/test-sources.txt
"${JAVA_HOME:+$JAVA_HOME/bin/}javac" --release 17 -Xlint:all -Werror -encoding UTF-8 -cp build/classes -d build/test-classes @build/test-sources.txt
"${JAVA_HOME:+$JAVA_HOME/bin/}java" -cp build/classes:build/test-classes kz.aitu.builder.ReportBuilderTest
