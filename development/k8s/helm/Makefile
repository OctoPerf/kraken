NAMESPACE ?= octoperf
CHART ?= kraken-dev
IPS := $(shell hostname -I)
IP := $(word 1, $(IPS))
VERSION := $(shell cat ../../../version.txt)

default: debug

dependency-update:
	helm dependency update ./$(CHART)

update-version:
	sed -i 's/\&default_tag\s.*/\&default_tag $(VERSION)/g' kraken-dev/values.yaml

update-ip:
	sed -i 's/\&kraken_local_ip\s.*/\&kraken_local_ip $(IP)/g' kraken-dev/values.yaml

debug: update-ip update-version
	helm install --debug --dry-run --namespace $(NAMESPACE) $(CHART) ./$(CHART) > debug.log

install: update-ip update-version
	-kubectl create namespace $(NAMESPACE)
	helm install --namespace $(NAMESPACE) $(CHART) ./$(CHART)
	kubectl wait --namespace $(NAMESPACE) --for=condition=ready pod --all --timeout=90s

clean:
	-helm delete $(CHART) --namespace $(NAMESPACE)
	-kubectl delete namespace $(NAMESPACE)