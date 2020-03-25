VERSION := $(shell cat ../version.txt)
KRAKEN_HOST_UID := $(id -u)
KRAKEN_HOST_GID := $(id -g)
KRAKEN_HOST_UNAME := $(id -un)
KRAKEN_HOST_GNAME := $(id -gn)
KRAKEN_HOST_DATA := $(pwd)/data
KRAKEN_DATA := /home/kraken/data

default: up

up:
	mkdir -p keycloak/postgres
	cd keycloak && docker-compose up --abort-on-container-exit

down:
	cd keycloak && docker-compose down
	sudo rm -rf keycloak/postgres/data/

export:
	docker exec -it kraken-keycloak-dev /opt/jboss/keycloak/bin/standalone.sh \
	-Djboss.socket.binding.port-offset=100 \
	-Dkeycloak.migration.action=export \
	-Dkeycloak.migration.provider=singleFile \
	-Dkeycloak.migration.realmName=kraken \
	-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
	-Dkeycloak.migration.file=/opt/jboss/keycloak/imports/kraken-realm.json
