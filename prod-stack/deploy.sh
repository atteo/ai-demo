#!/usr/bin/env bash
set -euo pipefail

REMOTE="ai-demo"
REMOTE_DIR="/opt/ai-demo"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "==> Building frontend"
cd "$REPO_ROOT/frontend"
bun run build
docker build -t ai-demo-frontend .

echo "==> Building backend"
cd "$REPO_ROOT/backend"
mvn package -DskipTests
docker build -t ai-demo-backend .

echo "==> Saving images"
docker save ai-demo-frontend ai-demo-backend | gzip > /tmp/ai-demo-images.tar.gz

echo "==> Uploading to $REMOTE"
ssh "$REMOTE" "mkdir -p $REMOTE_DIR"
scp /tmp/ai-demo-images.tar.gz "$REMOTE:$REMOTE_DIR/"
scp "$SCRIPT_DIR/docker-compose.yml" "$REMOTE:$REMOTE_DIR/"

echo "==> Deploying"
ssh "$REMOTE" "
  cd $REMOTE_DIR
  docker load < ai-demo-images.tar.gz
  docker compose up -d --remove-orphans
  rm ai-demo-images.tar.gz
"

rm /tmp/ai-demo-images.tar.gz
echo "==> Done — https://ai-demo.atteo.com"
