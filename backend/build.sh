#!/usr/bin/env bash
set -euo pipefail

mvn package -DskipTests
docker build -t ai-demo-backend .
