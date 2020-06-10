FROM openjdk:11-jdk-slim as build
RUN mkdir /sources
WORKDIR /sources
COPY . .
RUN ./gradlew shadowJar


FROM openjdk:11-jre-slim
RUN mkdir /app
COPY --from=build /sources/build/libs/ktor-demo-all.jar /app/
WORKDIR /app
CMD ["java", "-server", "-jar", "ktor-demo-all.jar"]
