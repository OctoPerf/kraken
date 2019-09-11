#!/usr/bin/env bash

export GATLING_COMMAND="--headless true --mode Har --har-file user-files/simulations/$HAR_PATH --package $GATLING_SIMULATION_PACKAGE --class-name $GATLING_SIMULATION_CLASS"

echo Remove previous record container

docker-compose down

echo Run gatling recorder with command $GATLING_COMMAND

docker-compose up --no-color --abort-on-container-exit --exit-code-from kraken-gatling-recorder
