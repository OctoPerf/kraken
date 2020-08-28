.EXPORT_ALL_VARIABLES:

VERSION := $(shell cat ../version.txt)
KRAKEN_VERSION := $(VERSION)
KRAKEN_LICENSE_ENTERPRISE := ../ee/license.jwt
SPRING_PROFILES_ACTIVE := dev

clean-all:
	./mvnw clean

test-all:
	./mvnw clean test -fn

test-all-fast:
	./mvnw -T 1C clean test -ff

dependencies:
	./mvnw dependency:analyze

build-fast:
	./mvnw -T 1C install -DskipTests

# Runs a Spring boot application (add --info in case of error)
# Example: make serve APP=applications/application-docker-all
serve: build-fast
	./mvnw spring-boot:run -pl $(APP)

serve-docker-all:
	$(MAKE) serve APP=applications/application-docker-all

serve-kubernetes-all:
	$(MAKE) serve APP=applications/ee/application-kubernetes-all

# Creates a Spring boot application jar
# Example: make build APP=applications/application-docker-all
bootJar:
	./mvnw package spring-boot:repackage -pl $(APP)

echo:
	@echo $(VERSION)


# SONAR
sonar-start:
	docker run -d --name sonarqube -p 9000:9000 sonarqube:lts

sonar:
	./mvnw clean verify -fn
	./mvnw sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml,../target/site/jacoco-aggregate/jacoco.xml