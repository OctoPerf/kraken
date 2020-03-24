#!/usr/bin/env bash

export KRAKEN_VERSION=1.0.0
export KRAKEN_HOST_UID=$(id -u)
export KRAKEN_HOST_GID=$(id -g)
export KRAKEN_HOST_UNAME=$(id -un)
export KRAKEN_HOST_GNAME=$(id -gn)
export KRAKEN_HOST_DATA=$(pwd)/data
export KRAKEN_DATA=/home/kraken/data

echo KRAKEN_VERSION=$KRAKEN_VERSION
echo KRAKEN_HOST_UID=$KRAKEN_HOST_UID
echo KRAKEN_HOST_GID=$KRAKEN_HOST_GID
echo KRAKEN_HOST_UNAME=$KRAKEN_HOST_UNAME
echo KRAKEN_HOST_GNAME=$KRAKEN_HOST_GNAME
echo KRAKEN_HOST_DATA=$KRAKEN_HOST_DATA
echo KRAKEN_DATA=$KRAKEN_DATA

# Must have added a group named docker https://docs.docker.com/install/linux/linux-postinstall/

mkdir -p analysis/grafana analysis/influxdb

docker-compose up --abort-on-container-exit