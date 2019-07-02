.PHONY: build-prod

# Node.js and Docker are required to run this Makefile

setup:
	sudo npm install -g @angular/cli
	sudo npm i -g npm-check-updates
	sudo npm i -g http-server
	npm install

clean:
	rm -rf ./dist

check:
	npm run check

update:
	npm run update

test:
	ng test --watch=false --project=$(APP)

# use 'fit' instead of 'it' with auto run tests to debug particular unit tests
test-auto:
	ng test --source-map=false --project=$(APP)

test-coverage:
	ng test --watch=false --code-coverage --source-map --project=$(APP)
	google-chrome ./coverage/$(APP)/index.html &

test-coverage-all:
	./test-coverage-all.sh

tslint: lint
lint:
	ng lint --fix

serve:
	ng serve --open --aot --project=$(APP) --base-href /$(APP)/

# You may need to update the property "sourceMap": true, in the angular.json file
serve-prod:
	npm run ng-high-memory -- serve --open --project=$(APP) --prod --sourceMap=true --base-href /$(APP)/

build-prod: clean
	npm run ng-high-memory -- build $(APP) --prod --base-href /$(APP)/

build-test: clean
	npm run ng-high-memory -- build $(APP) --prod --sourceMap=true
	http-server ./dist/$(APP) -o -p 8181
