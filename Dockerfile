# Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk11

# Add Maintainer Info
LABEL maintainer="rohit.phatak@synerzip.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8084

# The application's jar file
ARG JAR_FILE=build/libs/authentication-service-0.0.1-SNAPSHOT.jar

# Copy license store
COPY src/main/resources/license-store.txt /tmp

# Add the application's jar to the container
ADD ${JAR_FILE} authentication-service-0.0.1-SNAPSHOT.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/authentication-service-0.0.1-SNAPSHOT.jar","--LICENSE_STORE_FILE_PATH=/tmp","--LICENSE_STORE_FILE_NAME=license-store.txt"]
