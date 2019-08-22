# Docker Client

This library uses [docker-compose](https://docs.docker.com/compose/) and [Spotify's Docker-Client](https://github.com/spotify/docker-client/blob/master/docs/user_manual.md) to handle docker containers.

## Docker compose

The docker-compose client needs a project name and a working directory that contains all relevant files:

- docker-compose.yml,
- env files,
- mapped volumes directories and files,
- etc.

These parameters can either be provided using an application.yml properties file:

```yaml
kraken:
  docker:
    project:
      name: test
    directory: docker-root-test
```

Or using environment variables:

- KRAKEN_DOCKER_PROJECT_NAME
- KRAKEN_DOCKER_DIRECTORY

## Docker client

Spotify's docker-client library uses the following environment variable for initialization:

- DOCKER_HOST
- DOCKER_CERT_PATH