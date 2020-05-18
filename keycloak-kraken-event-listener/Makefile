.EXPORT_ALL_VARIABLES:

package:
	mvn package

deploy-dev: package
	cp target/kraken-keycloak-event-listener.jar ../development/keycloak/deployments/kraken-keycloak-event-listener.jar

test:
	mvn test