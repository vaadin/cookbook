# Build stage
FROM maven:3-jdk-11 as build
RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs
WORKDIR /usr/src/app/
COPY pom.xml .
RUN mvn dependency:go-offline -Pproduction

COPY src src
COPY frontend frontend
COPY package.json .
COPY pnpm-lock.yaml .
COPY parseClientRoutes.ts .
COPY parseClientRoutes.js .
COPY webpack.config.js .
RUN mvn clean package -DskipTests -Pproduction

# Run stage
FROM openjdk:11
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD java -jar /usr/app/app.jar