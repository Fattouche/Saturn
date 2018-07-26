# saturn

## Running Saturn
1. `docker-compose up --build`

## Running tests
1. Ensure Saturn is running (see above)
2. `cd test`
3. `docker-compose up --build`

## Alternative methods for running stuff

Only use these if you hate things that are easy

### Running tests with Docker directly
1. Ensure you have the site up and running (following instructions below) and run these commands from inside the `test` folder.
2. Start selenium server `docker run -d --name selenium --link saturn --shm-size 2g selenium/standalone-firefox:3.12.0-boron`
3. Build the test image `docker build -t saturn_test .`
4. Run the saturn tests `docker run --name saturn_test --link selenium -v ${PWD}/..:/saturn saturn_test`

### Running Saturn with Docker directly (will take long first time and cache from then on)
1. Pull a mysql docker image: `docker pull mysql`
2. Start mysql `docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=saturn -e MYSQL_USER=saturn -e MYSQL_PASSWORD=saturn -e MYSQL_DATABASE=saturn -p 3306:3306 mysql mysqld --default-authentication-plugin=mysql_native_password`
3. Optionally test db connection `mysql -u saturn -p` (password is saturn)
3. Build the saturn web server image `docker build -t saturn .`
4. Run the saturn web server (**The mysql container must be running before this**) `docker run -p 8080:8080 --name saturn --link mysql:mysql -v ${PWD}:/saturn saturn`

### Saturn Manual Setup

#### Required Software

1. Java 8 or greater
2. NPM 2.13 or greater
3. Maven 3.3 or greater
4. MySQL 5.5 or greater

#### Configuration

To configure the system:

1. Run `npm install` from inside the application's folder
2. Create database schema for the application with the name of your preference (the tables are created and populated automatically during the first run)
3. Open the configuration file `config/application-dev.yml` and set:
   1. `spring.datasource.url` to the database host, port and schema (defaults to **localhost**, **3306** and **saturn** respectively).
   2. `spring.datasource.username` to the database username (defaults to **saturn**)
   3. `spring.datasource.password` to the password of the database user set on previous step (defaults to **saturn**)
   4. `server.port` with server port (defaults to **8080**)
4. Set environment variable `ENV_PASSWDDB` to the desired database root password
5. Set environment variable `SATURN_VAULT_KEY` to the desired SaturnVault encryption key

After that execute `mvn spring-boot:run` from _inside_ the application's folder to run the application and navigate to [http://localhost:8080](http://localhost:8080) in your browser (note the port number might be different according to your configuration and sudo might be necessary if the application is configured to run on a port below 1024).

## Distribution

#### Building package

To build a distribution package:

1. Run `mvn clean package`
2. Copy `target/saturn-1.0.4.war` file to a new folder
3. Inside this folder create two subfolders, one called `config` and one called `mediaResources`
4. Copy `src/main/resources/config/application-dev.yml` into the `config` folder

#### Running package

To run the distribution packaged application:

1. Create database schema for the application with the name of your preference (the tables are created and populated automatically during the first run)
2. Configure the database and server port in the `config/application-dev.yml` file as explained above
3. Run `java -jar saturn-1.0.4.war` (sudo might be necessary if the application is configured to run on a port below 1024)

#### Testing Package

1. Start the saturn server
2. set the driver export variables
    1. export SATURN_DRIVER=chrome
    2. export SATURN_URL=http://localhost:8080  
3. run mvn  -Dtest=TestSelenium test



Finally navigate to [http://localhost:8080](http://localhost:8080) in your browser (note the port number might be different according to your configuration and deployment environment).

