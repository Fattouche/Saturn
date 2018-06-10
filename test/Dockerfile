FROM jimador/docker-jdk-8-maven-node
WORKDIR /saturn

# This should work without the -Dtest but it doesn't.
# We need to fix that or else we can't make new test files.
CMD ["mvn", "-Dtest=TestSelenium", "test"]
