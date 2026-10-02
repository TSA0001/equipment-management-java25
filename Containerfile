# syntax=docker/dockerfile:1.4
# Podman では docker 形式でビルドすること:
#   BUILDAH_FORMAT=docker podman-compose up --build -d

# ---- build stage ----
FROM docker.io/library/maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /workspace
COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests=false clean package

# ---- runtime stage ----
# Java 25 を含む IBM WebSphere Application Server Liberty 実行イメージ。
FROM icr.io/appcafe/open-liberty:26.0.0.9-full-java25-openj9-ubi-minimal

ENV TZ=Asia/Tokyo \
    DB_PATH=/data/h2/equipment

COPY --chown=1001:0 src/main/liberty/config/server.xml /config/server.xml
COPY --chown=1001:0 --from=build /workspace/target/equipment-management.war \
     /config/apps/equipment-management.war

EXPOSE 9080
