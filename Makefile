launch-docker:
	#start grafana and influxdb
	gnome-terminal --tab -- /bin/sh -c 'cd development; make up'
	
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
	gnome-terminal --tab -- /bin/sh -c 'cd development; make up'

	#start backend
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-storage'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-analysis'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-runtime-kubernetes'
	gnome-terminal --tab -- /bin/sh -c 'cd backend; make serve-sse'
	
	#start frontend
	gnome-terminal --tab -- /bin/sh -c 'cd frontend; make serve APP=administration'
	gnome-terminal --tab -- /bin/sh -c 'cd frontend; make serve APP=gatling'

git-help:
	@echo "git-init : Install git-flow, initialise repositories."
	@echo "git-pull BRANCH=branch_name : Checkout the branch, fetch it and pull it, for submodules too."
	@echo "git-push BRANCH=branch_name : Push the branch and the branch on submodules."
	@echo "git-branch-start BRANCH=branch_name : Create a branch."
	@echo "git-commit-push MESSAGE=message_txt BRANCH=branch_name : Commit and push."
	@echo "git-squash BRANCH=branch_name MESSAGE=\"message_txt\" : Replace all commits on a branch by new message and push the branch."
	@echo "git-branch-finish BRANCH=branch_name : Merge the branch in develop but not push. You must run : make git-push BRANCH=develop"

#list_submodules := backend/commons/ee backend/applications/ee deployment documentation
#cmd_pull_submodule = $(foreach submodule,$(list_submodules),$(MAKE) git-pull-submodule SUBMODULE=$(submodule))
git-pull:
	git checkout $(BRANCH)
	git fetch --progress --prune --recurse-submodules=no origin
	git pull origin $(BRANCH)
	git submodule update
	$(MAKE) git-pull-submodule SUBMODULE=backend/commons/ee BRANCH=$(BRANCH)
	$(MAKE) git-pull-submodule SUBMODULE=backend/applications/ee BRANCH=$(BRANCH)
	$(MAKE) git-pull-submodule SUBMODULE=deployment BRANCH=$(BRANCH)
	$(MAKE) git-pull-submodule SUBMODULE=documentation BRANCH=$(BRANCH)

git-pull-submodule:
	cd $(SUBMODULE);git checkout $(BRANCH)
	cd $(SUBMODULE);git fetch --progress --prune --recurse-submodules=no origin
	cd $(SUBMODULE);git pull origin $(BRANCH)

git-push:
	git push origin $(BRANCH)
	$(MAKE) git-push-submodule SUBMODULE=backend/commons/ee BRANCH=$(BRANCH)
	$(MAKE) git-push-submodule SUBMODULE=backend/applications/ee BRANCH=$(BRANCH)
	$(MAKE) git-push-submodule SUBMODULE=deployment BRANCH=$(BRANCH)
	$(MAKE) git-push-submodule SUBMODULE=documentation BRANCH=$(BRANCH)

git-push-submodule:
	git push origin $(BRANCH)

git-init:
	sudo apt-get update -y
	sudo apt-get install -y git-flow
	git config --global pull.rebase true
	git submodule init
	git submodule update
	git flow init -d
	$(MAKE) git-init-submodule SUBMODULE=backend/commons/ee
	$(MAKE) git-init-submodule SUBMODULE=backend/applications/ee
	$(MAKE) git-init-submodule SUBMODULE=deployment
	$(MAKE) git-init-submodule SUBMODULE=documentation

git-init-submodule:
	cd $(SUBMODULE);git flow init -d

git-branch-start:
	$(MAKE) git-pull BRANCH=develop
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
	-git add -A; git commit -a -m "$(MESSAGE)"; git flow feature publish $(BRANCH)

git-commit-push-submodule:
	cd $(SUBMODULE);git add -A; git commit -a -m "$(MESSAGE)"; git flow feature publish $(BRANCH)

git-branch-finish:
	-$(MAKE) git-branch-finish-submodule SUBMODULE=backend/commons/ee BRANCH=$(BRANCH)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=backend/applications/ee BRANCH=$(BRANCH)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=deployment BRANCH=$(BRANCH)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=documentation BRANCH=$(BRANCH)
	git flow feature finish $(BRANCH)

git-branch-finish-submodule:
	cd $(SUBMODULE);git flow feature finish $(BRANCH)

git-squash:
	-$(MAKE) git-squash-submodule SUBMODULE=backend/commons/ee MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	-$(MAKE) git-squash-submodule SUBMODULE=backend/applications/ee MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	-$(MAKE) git-squash-submodule SUBMODULE=deployment MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	-$(MAKE) git-squash-submodule SUBMODULE=documentation MESSAGE="$(MESSAGE)" BRANCH=$(BRANCH)
	git checkout feature/$(BRANCH);git reset $$(git merge-base develop $$(git rev-parse --abbrev-ref feature/$(BRANCH)));git add -A;git commit -m "$(MESSAGE)";git push -f origin feature/$(BRANCH)
	
git-squash-submodule:
	cd $(SUBMODULE);git checkout feature/$(BRANCH);git reset $$(git merge-base develop $$(git rev-parse --abbrev-ref feature/$(BRANCH)));git add -A;git commit -m "$(MESSAGE)";git push -f origin feature/$(BRANCH)
