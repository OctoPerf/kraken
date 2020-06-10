.EXPORT_ALL_VARIABLES:

package:
	mvn package

deploy: package
	cp target/kraken-keycloak-event-listener.jar ../deployment/keycloak/deployments/kraken-keycloak-event-listener.jar

test:
	mvn test