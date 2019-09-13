.EXPORT_ALL_VARIABLES:

VERSION := $(shell cat ../version.txt)
KRAKEN_VERSION := $(VERSION)

clean-all:
	./gradlew clean

test-all:
	./gradlew jacocoRootReport

check: test-all
	./gradlew check --warning-mode=all

scan:
	./gradlew build --scan

# Runs tests continuously for a project
# Example: make test APP=:commons:runtime:docker:docker-client
test:
	./gradlew $(APP):test --continuous

# Runs a Spring boot application (add --info in case of error)
# Example: make serve APP=:applications:app-analysis
serve:
	./gradlew $(APP):bootRun --args='--spring.profiles.active=dev'

copy-data:
	rm -rf applications/storage/data
	cp -a applications/storage/defaultData/. applications/storage/data/

serve-gatling-runner:
	$(MAKE) serve APP=:applications:app-gatling-runner

serve-analysis:
	$(MAKE) serve APP=:applications:app-analysis

serve-runtime-docker:
	$(MAKE) serve APP=:applications:app-runtime-docker

serve-runtime-mock:
	$(MAKE) serve APP=:applications:app-runtime-mock

serve-storage:
	$(MAKE) serve APP=:applications:app-storage

serve-static:
	docker run --rm --name kraken-static-dev -v $(KRAKEN_HOST_DATA):/usr/share/nginx/html:ro -p 8088:80 octoperf/kraken-static

clean:
	./gradlew $(APP):clean

# Creates a Spring boot application jar
# Example: make build APP=:applications:analysis
bootJar:
	./gradlew $(APP):bootJar

echo:
	@echo $(VERSION)