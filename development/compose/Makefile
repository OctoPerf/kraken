VERSION := $(shell cat ../../version.txt)
KRAKEN_HOST_UID := $(id -u)
KRAKEN_HOST_GID := $(id -g)
KRAKEN_HOST_UNAME := $(id -un)
KRAKEN_HOST_GNAME := $(id -gn)
KRAKEN_HOST_DATA := $(pwd)/data/kraken
KRAKEN_DATA := /home/kraken/data
IPS := $(shell hostname -I)
IP := $(word 1, $(IPS))

default: up

up: export KRAKEN_LOCAL_IP = ${IP}
up:
	mkdir -p data/analysis/grafana data/analysis/influxdb data/postgres
	docker-compose up --abort-on-container-exit

down:
	docker-compose down
	sudo rm -rf data/

export-kc:
	docker exec -it kraken-keycloak-dev /opt/jboss/keycloak/bin/standalone.sh \
	-Djboss.socket.binding.port-offset=200 \
	-Dkeycloak.migration.action=export \
	-Dkeycloak.migration.provider=singleFile \
	-Dkeycloak.migration.realmName=kraken \
	-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
	-Dkeycloak.migration.file=/opt/jboss/keycloak/imports/kraken-realm.json