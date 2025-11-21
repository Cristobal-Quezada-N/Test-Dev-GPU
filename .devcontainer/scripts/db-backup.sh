#!/bin/bash
set -e

BCK_DIR="./backups"
BCK_DATE="$(date +%F_%H-%M)"

set -a
source ./.env.devcontainer
set +a

# PgAdmin Backup
SRC_PGADMIN="/var/lib/pgadmin/pgadmin4.db"
BCK_PGADMIN="$BCK_DIR/pgadmin4-$BCK_DATE.db"

echo "[*] Backup de PgAdmin $BCK_PGADMIN..."
docker cp pgadmin-devcontainer:$SRC_PGADMIN $BCK_PGADMIN

# Postgres Backup
SRC_PG="/tmp/postgres-$BCK_DATE.dump"
BCK_PG="$BCK_DIR/postgres-$BCK_DATE.dump"

echo "[*] Backup de Postgres $SRC_PG..."
docker exec -t postgres-devcontainer pg_dump -Fc --file="$SRC_PG"
docker cp postgres-devcontainer:$SRC_PG $BCK_PG
