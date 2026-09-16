#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
bash scripts/build.sh
"${JAVA_HOME:+$JAVA_HOME/bin/}java" -cp build/classes kz.aitu.builder.Main "$@"
