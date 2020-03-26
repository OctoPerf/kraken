launch-docker:
	#start grafana and influxdb
	gnome-terminal --tab -- /bin/sh -c 'cd development; ./up.sh'
	
	#start backend
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-storage'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-analysis'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-runtime-docker'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-sse'
	
	#start frontend
	gnome-terminal --tab -- /bin/sh -c 'cd frontend; make serve APP=administration'
	gnome-terminal --tab -- /bin/sh -c 'cd frontend; make serve APP=gatling'

launch-k8s:
	#start kind
	$(MAKE) -C deployment/k8s kind-serve
	
	#start grafana and influxdb
	gnome-terminal --tab -- /bin/sh -c 'cd development; ./up.sh'

	#start backend
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-storage'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-analysis'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-runtime-kubernetes'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-sse'
	
	#start frontend
	gnome-terminal --tab -- /bin/sh -c 'cd frontend; make serve APP=administration'
	gnome-terminal --tab -- /bin/sh -c 'cd frontend; make serve APP=gatling'

list_submodules := backend/commons/ee backend/applications/ee deployment documentation
cmd_pull_submodule = $(foreach submodule,$(list_submodules),$(MAKE) git-pull-submodule SUBMODULE=$(submodule))
git-pull-master:
	git checkout master
	git fetch --progress --prune --recurse-submodules=no origin
	git pull
	git submodule update
	$(MAKE) git-pull-submodule SUBMODULE=backend/commons/ee
	$(MAKE) git-pull-submodule SUBMODULE=backend/applications/ee
	$(MAKE) git-pull-submodule SUBMODULE=deployment
	$(MAKE) git-pull-submodule SUBMODULE=documentation

git-pull-submodule:
	cd $(SUBMODULE);pwd
	cd $(SUBMODULE);git checkout master
	cd $(SUBMODULE);git fetch --progress --prune --recurse-submodules=no origin
	cd $(SUBMODULE);git pull

git-init:
	sudo apt-get update -y
	sudo apt-get install -y git-flow
	git config --global pull.rebase true
	git flow init
	$(MAKE) git-init-submodule SUBMODULE=backend/commons/ee
	$(MAKE) git-init-submodule SUBMODULE=backend/applications/ee
	$(MAKE) git-init-submodule SUBMODULE=deployment
	$(MAKE) git-init-submodule SUBMODULE=documentation

git-init-submodule:
	cd $(SUBMODULE);git flow init

git-branch-start:
	git flow feature start $(BRANCH)
	$(MAKE) git-branch-start-submodule SUBMODULE=backend/commons/ee BRANCH=$(BRANCH)
	$(MAKE) git-branch-start-submodule SUBMODULE=backend/applications/ee BRANCH=$(BRANCH)
	$(MAKE) git-branch-start-submodule SUBMODULE=deployment BRANCH=$(BRANCH)
	$(MAKE) git-branch-start-submodule SUBMODULE=documentation BRANCH=$(BRANCH)

git-branch-start-submodule:
	cd $(SUBMODULE);git flow feature start $(BRANCH)

git-commit-push:
	-$(MAKE) git-commit-push-submodule SUBMODULE=backend/commons/ee MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	-$(MAKE) git-commit-push-submodule SUBMODULE=backend/applications/ee MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	-$(MAKE) git-commit-push-submodule SUBMODULE=deployment MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	-$(MAKE) git-commit-push-submodule SUBMODULE=documentation MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	-git commit -a -m "$(MESSAGE)"; git flow feature publish $(BRANCH)

git-commit-push-submodule:
	cd $(SUBMODULE);git commit -a -m "$(MESSAGE)"; git flow feature publish $(BRANCH)

git-branch-finish:
	-$(MAKE) git-branch-finish-submodule SUBMODULE=backend/commons/ee BRANCH=$(BRANCH)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=backend/applications/ee BRANCH=$(BRANCH)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=deployment BRANCH=$(BRANCH)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=documentation BRANCH=$(BRANCH)
	git flow feature finish $(BRANCH)

git-branch-finish-submodule:
	cd $(SUBMODULE);git flow feature finish $(BRANCH)
