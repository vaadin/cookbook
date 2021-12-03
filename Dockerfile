# Build stage
FROM openjdk:17-jdk-slim as build
RUN apt-get update -qq && apt-get install -qq --no-install-recommends maven
RUN useradd -m myuser
WORKDIR /usr/src/app/
RUN chown myuser:myuser /usr/src/app/
USER myuser
COPY --chown=myuser pom.xml ./

# This allows repeated builds to start from the next step, with all Maven dependencies cached
RUN mvn dependency:go-offline -Pproduction

COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser frontend frontend
COPY --chown=myuser package.json pnpm-lock.yaml parseClientRoutes.ts webpack.config.js ts-routes.ts.template ./
RUN mvn clean package -DskipTests -Pproduction

# Run stage
FROM openjdk:17-jdk-slim
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD java -jar /usr/app/app.jar