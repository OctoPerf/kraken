#!/usr/bin/env bash

docker save $1 | (eval $(minikube docker-env) && docker load)