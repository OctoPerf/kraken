#!/usr/bin/env bash

echo Simulation $GATLING_SIMULATION CANCELLING

docker-compose down

echo Simulation $GATLING_SIMULATION CANCELED
curl -X POST \
      "$KRAKEN_ANALYSIS_URL/test/status/CANCELED?testId=$KRAKEN_TEST_ID" \
      -H 'cache-control: no-cache'
printf "\n"
