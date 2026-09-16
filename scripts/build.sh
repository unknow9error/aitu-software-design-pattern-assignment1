#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p build/classes
find src/main/java -name '*.java' | sort > build/main-sources.txt
"${JAVA_HOME:+$JAVA_HOME/bin/}javac" --release 17 -Xlint:all -Werror -encoding UTF-8 -d build/classes @build/main-sources.txt
