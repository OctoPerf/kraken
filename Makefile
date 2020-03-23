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
