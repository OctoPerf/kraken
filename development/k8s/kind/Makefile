.EXPORT_ALL_VARIABLES:
.PHONY: docker-all

VERSION := $(shell cat ../../../version.txt)
default: kind-serve

# https://kind.sigs.k8s.io/docs/user/quick-start/
# > curl -Lo ./kind https://github.com/kubernetes-sigs/kind/releases/download/v0.5.1/kind-linux-amd64
# > chmod +x ./kind
# > sudo mv ./kind /usr/local/bin/kind

# WARNING Kind v7.0.0 prevents the kraken-runtime-kubernetes to make any request (SSL related errors)
kind-install:
	curl -Lo ./kind https://github.com/kubernetes-sigs/kind/releases/download/v0.7.0/kind-linux-amd64
	chmod +x ./kind
	sudo mv ./kind /usr/local/bin/kind

kind-serve: kind-clean kind-start kind-ingress kind-docker-copy-all

kind-start:
	kind create cluster --config kind-config.yaml

# https://kind.sigs.k8s.io/docs/user/ingress/
kind-ingress:
	kubectl apply -f mandatory.yaml
	kubectl patch deployments -n ingress-nginx nginx-ingress-controller -p '{"spec":{"template":{"spec":{"containers":[{"name":"nginx-ingress-controller","ports":[{"containerPort":80,"hostPort":80},{"containerPort":443,"hostPort":443}]}],"nodeSelector":{"ingress-ready":"true"},"tolerations":[{"key":"node-role.kubernetes.io/master","operator":"Equal","effect":"NoSchedule"}]}}}}'
	kubectl apply -f service-nodeport.yaml
# kubectl apply -f https://kind.sigs.k8s.io/examples/ingress/usage.yaml

kind-docker-copy-all:
	kind load docker-image octoperf/kraken-storage:$(VERSION)
	kind load docker-image octoperf/kraken-analysis:$(VERSION)
	kind load docker-image octoperf/kraken-sse:$(VERSION)
	kind load docker-image octoperf/kraken-gatling-runner:$(VERSION)
	kind load docker-image octoperf/kraken-gatling-log-parser:$(VERSION)
	kind load docker-image octoperf/kraken-telegraf:$(VERSION)
	kind load docker-image octoperf/kraken-gatling-recorder:$(VERSION)
	kind load docker-image octoperf/kraken-har-parser:$(VERSION)
	kind load docker-image octoperf/kraken-ee-runtime-kubernetes:$(VERSION)
	kind load docker-image octoperf/kraken-documentation:$(VERSION)
	kind load docker-image octoperf/kraken-gatling-ui:$(VERSION)
	kind load docker-image octoperf/kraken-administration-ui:$(VERSION)
	kind load docker-image octoperf/kraken-echo:$(VERSION)

kind-clean:
	-kind delete cluster