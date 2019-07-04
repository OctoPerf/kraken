# Kraken - The Load Testing IDE

Kraken by [OctoPerf](https://octoperf.com) is a [load testing IDE](https://kraken.octoperf.com/).

![Kraken Gatling](https://raw.githubusercontent.com/OctoPerf/kraken/master/kraken-preview.gif "Kraken Gatling")

More information in [our load testing blog](https://octoperf.com/categories/kraken/)

## Documentation

* [Getting Started](https://kraken.octoperf.com/)
* [Download](https://kraken.octoperf.com/download/)
* [Install](https://kraken.octoperf.com/install/linux/)
* [Gatling Integration](https://kraken.octoperf.com/gatling/)
* [Administration](https://kraken.octoperf.com/administration/)


## Development

### Source code structure

The source code of Kraken is divided in several folders:

* `backend`: this folder contains a Gradle multi-modules project that build all Java backends of Kraken,
* `deployment`: private Git sub-module used to create and deploy all Docker containers,
* `development`: contains shell scripts and docker-compose configuration to start the InfluxDb/Grafana stack in dev mode,
* `documentation`: private Git sub-module that builds Kraken's documentation,
* `frontend`: this folder contains an Angular project with several libraries and two applications: administration and gatling.

### Prerequisites

* Requires the **make** command `sudo apt-get install build-essential`
* Requires [docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
* Requires [docker compose](https://docs.docker.com/compose/install/#install-compose)
* Requires the [JDK 11 ](https://openjdk.java.net/projects/jdk/11/)

### Run the application from the source code

To run Kraken from the source code you need to:

1. Start the development stack of InfluxDB/Grafana
  * `cd development`
  * `./up.sh`
2. Start the backend servers
  * `cd backend`
  * `make copy-data` (one time only)
  * `make serve-storage`
  * `make serve-command`
  * `make serve-docker`
  * `make serve-static`
  * `make serve-analysis`
3. Start the frontend applications
  * `cd frontend`
  * `make setup` (one time only)
  * `make serve APP=gatling`
  * `make serve APP=administration`
  
![Kraken Dev Architecture](https://raw.githubusercontent.com/OctoPerf/kraken/master/kraken-dev-architecture.png "Kraken Dev Architecture")

### Tests and bug finders

#### Frontend

* `./test-coverage-all` to generate test coverage for the complete project,
* `make test APP=<library-or-app>` to run tests for a single module (for example `make test APP=commons`),
* `make lint` to run TSLint on the whole project.

#### Backend

* `make check` to run all unit tests and SpotBugs,
* `make test APP=<module-path>` to run unit test for a single sub-module (for example `make test APP=:commons:docker-client`).

### Roadmap

Check out the [next milestone](https://github.com/OctoPerf/kraken/milestones).

## License

Kraken is an open core product:

* The majority of the code base is licensed under the [Apache V2 License](https://www.apache.org/licenses/LICENSE-2.0),
* Some closed-source components, not available directly in this repository, requires a license (free or paid) from [OctoPerf](https://octoperf.com).

