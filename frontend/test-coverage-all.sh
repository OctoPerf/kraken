#!/bin/bash

echo "Start"

rm -rf coverage
rm -rf coverage-all

for dir in projects/*; do
  if [[ ("$dir" != "projects/ext") && ("$dir" != "projects/assets") && ("$dir" != "projects/styles") && ("$dir" != *-e2e)]]
  then
    prefix="projects/";
    project=${dir#$prefix}; #Remove prefix
    echo "$project"
    ng test --watch=false --code-coverage --source-map --project=$project &
  fi
done

wait  # Wait for all tasks to complete

./node_modules/istanbul/lib/cli.js report --dir ./coverage-all/ html

google-chrome ./coverage-all/index.html &

echo "Done"
