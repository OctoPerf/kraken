NAMESPACE ?= octoperf
CHART ?= kraken-dev
HELM_PARAMS=--name $(CHART) --namespace $(NAMESPACE)
HELM_REPO="https://helm.octoperf.com"

default: debug

dependency-update:
	helm dependency update ./$(CHART)

debug:
	helm install --debug --dry-run --namespace $(NAMESPACE) $(CHART) ./$(CHART) > debug.log

install:
	-kubectl create namespace $(NAMESPACE)
	helm install --namespace $(NAMESPACE) $(CHART) ./$(CHART)

clean:
	-helm delete $(CHART) --namespace $(NAMESPACE)
	-kubectl delete namespace $(NAMESPACE)
	-kubectl delete ClusterRole kraken-runtime
	-kubectl delete ClusterRoleBinding kraken-runtime
	-kubectl delete PodSecurityPolicy kraken-grafana kraken-grafana-test
	-kubectl delete ClusterRole kraken-grafana-clusterrole
	-kubectl delete ClusterRoleBinding kraken-grafana-clusterrolebinding