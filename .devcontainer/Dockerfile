FROM openjdk:8u342-jdk-bullseye

RUN apt-get update
RUN apt-get install -y \
    zsh git vim curl
RUN wget https://services.gradle.org/distributions/gradle-7.5.1-bin.zip -P /tmp

RUN unzip -d /opt/gradle /tmp/gradle-*.zip

RUN echo 'export GRADLE_HOME=/opt/gradle/gradle-7.5.1' > /etc/profile.d/gradle.sh
RUN echo 'export PATH=${GRADLE_HOME}/bin:${PATH}' >> /etc/profile.d/gradle.sh

RUN chmod +x /etc/profile.d/gradle.sh

RUN sh -c "$(wget -O- https://github.com/deluan/zsh-in-docker/releases/download/v1.1.2/zsh-in-docker.sh)" -- \
    -t robbyrussell