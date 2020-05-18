.PHONY: build-prod

# Node.js and Docker are required to run this Makefile

setup:
	sudo npm i -g @angular/cli
	sudo npm i -g npm-check-updates
	sudo npm i -g http-server
	sudo npm i -g webpack-bundle-analyzer
	npm install

clean:
	rm -rf ./dist

check:
	npm run check

update:
	npm run update

test:
	ng test --watch=false --project=$(APP)

test-all:
	ng test --watch=false

# use 'fit' instead of 'it' with auto run tests to debug particular unit tests
test-auto:
	ng test --sourceMap=false --project=$(APP)

test-coverage:
	ng test --watch=false --codeCoverage=true --sourceMap=true --project=$(APP)
	xdg-open ./coverage/$(APP)/index.html &

test-coverage-all:
	./test-coverage-all.sh

tslint: lint
lint:
	ng lint --fix

serve:
	npm run ng-high-memory -- serve --open --project=$(APP) --baseHref /$(APP)/ --host kraken.local

# You may need to update the property "sourceMap": true, in the angular.json file
serve-prod:
	npm run ng-high-memory -- serve --open --project=$(APP) --prod --sourceMap=true --baseHref /$(APP)/

build-prod: clean
	npm run ng-high-memory -- build $(APP) --prod --baseHref /$(APP)/

build-test: clean
	npm run ng-high-memory -- build $(APP) --prod --sourceMap=true
	http-server ./dist/$(APP) -o -p 8181

size-check:
	npm run ng-high-memory -- build $(APP) --stats-json
	webpack-bundle-analyzer ./dist/$(APP)/stats-es2015.json


