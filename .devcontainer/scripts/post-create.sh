#!/bin/sh
set -e

echo "System: $(uname -a)"
echo "Postgres: $(psql --version)"
echo "NPM: $(npm --version)"
echo "Node: $(node --version)"
