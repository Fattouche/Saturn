#Build
FROM jimador/docker-jdk-8-maven-node
COPY package.json .
RUN npm install
COPY pom.xml .
RUN ["mvn", "verify", "clean", "--fail-never", "-Denv.passwddb=saturn"]
COPY . .
RUN mvn package

#Execute
FROM openjdk:8-jdk-alpine
COPY --from=0 target/saturn-1.0.4.war /home/saturn-1.0.4.war
WORKDIR /saturn
COPY wait-for.sh .
EXPOSE 8080
