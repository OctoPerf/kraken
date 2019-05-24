FROM ubuntu:18.10

LABEL maintainer="gerald.pereira@octoperf.com"

RUN set -eux \
    && echo Update and install base packages \
    && apt-get update -y \
    && apt-get -y --no-install-recommends install openjdk-11-jre gosu apt-transport-https ca-certificates curl gnupg-agent software-properties-common \
    && echo Create home folders \
    && mkdir -p /home/kraken/config /home/kraken/data \
    && echo Install Docker \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - \
    && add-apt-repository \
          "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
          $(lsb_release -cs) \
          stable" \
    && apt-get update -y \
    && apt-get -y install docker-ce-cli containerd.io \
    && echo Install docker compose \
    && curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose \
    && chmod +x /usr/local/bin/docker-compose \
    && ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose \
    && echo Reduce image size \
    && apt-get -y autoremove --purge \
    && apt-get -y autoclean \
    && rm -rf /var/lib/apt/lists/*

ENV HOME /home/kraken

VOLUME ["${HOME}/config", "${HOME}/data"]

WORKDIR ${HOME}

CMD ["/bin/bash"]
