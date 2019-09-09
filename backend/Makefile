.EXPORT_ALL_VARIABLES:

kraken.version := $(shell cat ../version.txt)

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
	./gradlew $(APP):bootRun

copy-data:
	rm -rf applications/data
	cp -a applications/storage/defaultData/. applications/data/

serve-analysis:
	$(MAKE) serve APP=:applications:analysis

serve-runtime-docker:
	KRAKEN_DATA="test"
	$(MAKE) serve APP=:applications:app-runtime-docker

serve-storage:
	$(MAKE) serve APP=:applications:storage

serve-static:
	docker run --rm --name kraken-static-dev -v $(kraken.data):/usr/share/nginx/html:ro -p 8088:80 octoperf/kraken-static

clean:
	./gradlew $(APP):clean

# Creates a Spring boot application jar
# Example: make build APP=:applications:analysis
bootJar:
	./gradlew $(APP):bootJar

echo:
	@echo $(kraken.version)