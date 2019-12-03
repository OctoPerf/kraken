#!/bin/bash

echo "Start"

rm -rf coverage
rm -rf coverage-all

for dir in projects/*; do
  if [[("$dir" != *-e2e)]]
  then
    prefix="projects/";
    project=${dir#$prefix}; #Remove prefix
    echo "$project"
    # Remove the trailing & character to execute tests sequentially
    ng test --watch=false --codeCoverage=true --sourceMap=true --project=$project &
  fi
done

wait  # Wait for all tasks to complete

./node_modules/istanbul/lib/cli.js report --dir ./coverage-all/ html

google-chrome ./coverage-all/index.html &

echo "Done"
