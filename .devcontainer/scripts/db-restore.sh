#!/bin/bash
set -e

BCK_DATE="$(date +%F_%H-%M)"

set -a
source ./.env.devcontainer
set +a

# PgAdmin Backup
BCK_PGADMIN="$1"
DST_PGADMIN="/var/lib/pgadmin/pgadmin4.db"

echo "[*] Restaurando backup $(basename $BCK_PGADMIN) de PgAdmin..."
docker cp "$BCK_PGADMIN" pgadmin-devcontainer:$DST_PGADMIN

# Postgres Restaurar Backup
BCK_PG="$2"
DST_PG="/tmp/$(basename $BCK_PG)"

echo "[*] Restaurando backup $(basename $BCK_PG) de Postgres..."
docker cp "$BCK_PG" postgres-devcontainer:$DST_PG
docker exec -i postgres-devcontainer \
    pg_restore --dbname=$PG_DATABASE --if-exists --clean "$DST_PG"
