.EXPORT_ALL_VARIABLES:

VERSION := $(shell cat ../version.txt)
KRAKEN_VERSION := $(VERSION)

clean-all:
	./gradlew clean

test-all:
	./gradlew jacocoRootReport -Dorg.gradle.parallel=false

check: test-all
	./gradlew check --warning-mode=all

scan:
	./gradlew build --scan

dependencies:
	./gradlew $(APP):dependencies

# Runs tests continuously for a project
# Example: make test APP=:commons:runtime:docker:docker-client
test:
	./gradlew $(APP):test --continuous

# Runs a Spring boot application (add --info in case of error)
# Example: make serve APP=:applications:app-analysis
serve:
	./gradlew $(APP):bootRun --args='--spring.profiles.active=dev'

serve-gatling-runner:
	$(MAKE) serve APP=:applications:app-gatling-runner

serve-analysis:
	$(MAKE) serve APP=:applications:app-analysis

serve-runtime-docker:
	$(MAKE) serve APP=:applications:app-runtime-docker

serve-runtime-k8s:
	$(MAKE) serve APP=:applications:ee:app-runtime-kubernetes

serve-storage:
	$(MAKE) serve APP=:applications:app-storage

serve-sse:
	$(MAKE) serve APP=:applications:app-sse

clean:
	./gradlew $(APP):clean

# Creates a Spring boot application jar
# Example: make build APP=:applications:analysis
bootJar:
	./gradlew $(APP):bootJar

echo:
	@echo $(VERSION)