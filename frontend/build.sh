#!/usr/bin/env bash
set -e

bun run build
docker build -t ai-demo-frontend .
