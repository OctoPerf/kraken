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
	@echo "git-branch-start FEATURE=feature_name : Create a branch."
	@echo "git-commit-push MESSAGE=message_txt FEATURE=feature_name : Commit and push."
	@echo "git-squash FEATURE=feature_name MESSAGE=\"message_txt\" : Replace all commits on a branch by new message and push the branch."
	@echo "git-branch-finish FEATURE=feature_name : Merge the branch in develop but not push. You must run : make git-push BRANCH=develop"
	@echo "git-rebase FROM=from_branch_name TO=to_branch_name : rebase the branch 'FROM' on the branch 'TO' for current and submodules."
	@echo "git-pull-force FEATURE=feature_name : Checkout the branch, fetch it and reset hard it, for submodules too."

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
	git flow feature start $(FEATURE)
	$(MAKE) git-branch-start-submodule SUBMODULE=backend/commons/ee FEATURE=$(FEATURE)
	$(MAKE) git-branch-start-submodule SUBMODULE=backend/applications/ee FEATURE=$(FEATURE)
	$(MAKE) git-branch-start-submodule SUBMODULE=deployment FEATURE=$(FEATURE)
	$(MAKE) git-branch-start-submodule SUBMODULE=documentation FEATURE=$(FEATURE)

git-branch-start-submodule:
	cd $(SUBMODULE);git flow feature start $(FEATURE)

git-commit-push:
	-$(MAKE) git-commit-push-submodule SUBMODULE=backend/commons/ee MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	-$(MAKE) git-commit-push-submodule SUBMODULE=backend/applications/ee MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	-$(MAKE) git-commit-push-submodule SUBMODULE=deployment MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	-$(MAKE) git-commit-push-submodule SUBMODULE=documentation MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	-git add -A; git commit -a -m "$(MESSAGE)"; git flow feature publish $(FEATURE)

git-commit-push-submodule:
	cd $(SUBMODULE);git add -A; git commit -a -m "$(MESSAGE)"; git flow feature publish $(FEATURE)

git-branch-finish:
	-$(MAKE) git-branch-finish-submodule SUBMODULE=backend/commons/ee FEATURE=$(FEATURE)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=backend/applications/ee FEATURE=$(FEATURE)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=deployment FEATURE=$(FEATURE)
	-$(MAKE) git-branch-finish-submodule SUBMODULE=documentation FEATURE=$(FEATURE)
	git flow feature finish $(FEATURE)

git-branch-finish-submodule:
	cd $(SUBMODULE);git flow feature finish $(FEATURE)

git-squash:
	-$(MAKE) git-squash-submodule SUBMODULE=backend/commons/ee MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	-$(MAKE) git-squash-submodule SUBMODULE=backend/applications/ee MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	-$(MAKE) git-squash-submodule SUBMODULE=deployment MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	-$(MAKE) git-squash-submodule SUBMODULE=documentation MESSAGE="$(MESSAGE)" FEATURE=$(FEATURE)
	git checkout feature/$(FEATURE);git reset $$(git merge-base develop $$(git rev-parse --abbrev-ref feature/$(FEATURE)));git add -A;git commit -m "$(MESSAGE)";git push -f origin feature/$(FEATURE)
	
git-squash-submodule:
	cd $(SUBMODULE);git checkout feature/$(FEATURE);git reset $$(git merge-base develop $$(git rev-parse --abbrev-ref feature/$(FEATURE)));git add -A;git commit -m "$(MESSAGE)";git push -f origin feature/$(FEATURE)

git-rebase:
	-$(MAKE) git-rebase-submodule SUBMODULE=backend/commons/ee FROM=$(FROM) TO=$(TO)
	-$(MAKE) git-rebase-submodule SUBMODULE=backend/applications/ee FROM=$(FROM) TO=$(TO)
	-$(MAKE) git-rebase-submodule SUBMODULE=deployment FROM=$(FROM) TO=$(TO)
	-$(MAKE) git-rebase-submodule SUBMODULE=documentation FROM=$(FROM) TO=$(TO)
	git checkout $(FROM);git rebase origin/$(TO) $(FROM);git push origin $(FROM) --force
	
git-rebase-submodule:
	cd $(SUBMODULE);git checkout $(FROM);git rebase origin/$(TO) $(FROM);git push origin $(FROM) --force

git-pull-force:
	-$(MAKE) git-pull-force-submodule SUBMODULE=backend/commons/ee FEATURE=$(FEATURE)
	-$(MAKE) git-pull-force-submodule SUBMODULE=backend/applications/ee FEATURE=$(FEATURE)
	-$(MAKE) git-pull-force-submodule SUBMODULE=deployment FEATURE=$(FEATURE)
	-$(MAKE) git-pull-force-submodule SUBMODULE=documentation FEATURE=$(FEATURE)
	git checkout feature/$(FEATURE); git fetch origin feature/$(FEATURE); git reset --hard origin/feature/$(FEATURE)

git-pull-force-submodule:
	cd $(SUBMODULE);git checkout feature/$(FEATURE); git fetch origin feature/$(FEATURE); git reset --hard origin/feature/$(FEATURE)
