# Kraken - The Load Testing IDE

Kraken is a load testing IDE based on Gatling.

As such, Kraken provides a complete development environment to software programmers and load testers that seek to make the most out of Gatling:

* A code editor to create and update .scala Gatling simulations with autocomplete suggestions and code snippets,
* Simulations debugging and comparison with HAR imports,
* Load tests analysis with aggregated data in InfluxDb displayed in comprehensive reports generated with Grafana,
* Multi-hosts load injection using a Kubernetes cluster.

## Support and Community

### Blog

[Load Testing blog on OctoPerf](https://blog.octoperf.com/)

#### Tutorials

1. [Gatling - Getting Started with Simulation Scripts](https://blog.octoperf.com/gatling-getting-started-with-simulation-scripts/)
2. [Gatling - Simulation Scripts Parameterization](https://blog.octoperf.com/gatling-simulation-scripts-parameterization/)
2. [Gatling - Loops, Conditions and Pauses](https://blog.octoperf.com/gatling-loops-conditions-and-pauses/)

### Email

Contact us at [support@octoperf.com](mailto:support@octoperf.com).

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

To run Kraken from the source code, execute one of the following commands from the root folder of the repository:

* `make launch-docker` to run the Docker version,
* `make launch-k8s` to run the Kubernetes version. 

Open the [Makefile](https://github.com/OctoPerf/kraken/blob/master/Makefile) to know in detail what each of these tasks does.
  
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

