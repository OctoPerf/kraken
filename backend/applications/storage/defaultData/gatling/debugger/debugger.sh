#!/usr/bin/env bash

export GATLING_COMMAND="-s $GATLING_SIMULATION -rf results/$KRAKEN_TEST_ID -rd $GATLING_RUN_DESCRIPTION"

echo Simulation $GATLING_SIMULATION DEBUGGING
curl -X POST \
  "$KRAKEN_ANALYSIS_URL/test/status/RUNNING?testId=$KRAKEN_TEST_ID" \
  -H 'cache-control: no-cache'
printf "\n"

echo Remove previous debug containers

docker-compose down

echo Debug gatling with command $GATLING_COMMAND

docker-compose up --no-color --abort-on-container-exit --exit-code-from kraken-gatling-debugger

export EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ] ; then
    echo Simulation $GATLING_SIMULATION COMPLETED
    curl -X POST \
      "$KRAKEN_ANALYSIS_URL/test/status/COMPLETED?testId=$KRAKEN_TEST_ID" \
      -H 'cache-control: no-cache'
else
    echo Simulation $GATLING_SIMULATION FAILED
    curl -X POST \
      "$KRAKEN_ANALYSIS_URL/test/status/FAILED?testId=$KRAKEN_TEST_ID" \
      -H 'cache-control: no-cache'
fi
printf "\n"