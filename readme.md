Micro-services for calculating the shortest path
================================================

The goal of this project is to demonstrate the usage of microservices applied to calculate the shortest path between two cities.

## Technologies

The technologies used in this project are:

* **Gradle**: Used to automate the build of each microservice.
* **Spring Boot**: Used to implement the microservices and tests.
* **JUnit**: Used to implement tests.
* **Netflix Eureka**: Used to discover the microservices shared in the application.
* **H2**: Used as local database to store city information.
* **Tomcat**: Used as container to deploy the microservices.

## Structure

For this project it was created four microservices, as follows:

* **service-registration**: responsible for launching the Eureka server. Each microservice in this application must register itself in the service-registration. By doing this, the microservice will be visible for the other microservices.
* **city-catalog**: responsible for the city management. This microservice has the endpoints for adding, updating, and removing cities and the distances among them.
* **shortest-route-calculator**: reponsible for infering the shortest route between two cities. This microservices communicates with _city-catalog_ microservice to obtain city information.
* **facade-service**: act as a layer centralizing the user requests. It has communication with all registered microservices. It is by accessing this microservice that the user will interact with the application.

Each microservice is a complete independent project. This approach allows developers to change, for example, the service that calculate the shortest route without interfering in the service responsible by the cities management. 

## Running the application

After clonning this project you must build and launch each microservice.

  gradlew clean
  gradlew build

To launch the microservices, run the following commands:

  ```
  java -jar .\service-registration\build\libs\service-registration-0.1.0-SNAPSHOT.jar  
  java -jar .\city-catalog\build\libs\city-catalog-0.1.0-SNAPSHOT.jar  
  java -jar .\shortest-route-calculator\build\libs\shortest-route-calculator-0.1.0-SNAPSHOT.jar  
  java -jar .\facade-service\build\libs\facade-service-0.1.0-SNAPSHOT.jar  
  ```

Each one of the above commands will start a tomcat instance.

To check if all is working properly, access the [service-registration](http://localhost:1111) service.  
You will see a list of available microservices. If everything is OK, you will see the following microservices:

* CITY-SERVICE
* FACADE-SERVICE
* ROUTE-SERVICE

This implementation does not have graphical interface, so you have to execute POST commands using a tool like cURL. The GET commands can be executed directly in your browser.

Let's perform some tests to verify that everything is OK.  
The FACADE-SERVICE, used to interact with our application, runs at the port 3333.  
For these tests we will use the cURL tool.

**Adding two new cities**: 
To add a new city or a new connection we must use the operation **ADD**
  
  ```
  curl -H "Content-Type: application/json" -X POST -d "{"id":"501","name":"Garibaldi","Operation":"ADD"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"502","name":"Caxias do Sul","Operation":"ADD","distance":"55","to_id":"501"}" http://localhost:3333/router/city  
  ```

Running the first command, we added a new city without connections with other cities. When we run the second command, a new city is added and it is connected with the first one. Now we can check the content of each city and we will realize that the first city received automaticaly a connection with the second one.

  ```
  curl http://localhost:3333/router/city/501  
  ```

The answer of this command is:

  ```
  {"id":501,"name":"Garibaldi","distances":[{"toCity":502,"distance":55}]}  
  ```

**Updating a city**

We could, for example, change the name of a city or the distance to a connection. For that, we can use the operation **UPT**. In the below example we will add a new city (Carlos Barbosa) connected to Garibaldi and we will change some values, the name and the distance.

  ```
  curl -H "Content-Type: application/json" -X POST -d "{"id":"503","name":"Carlo Barbosa","Operation":"ADD","distance":"10","to_id":"501"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"503","name":"Carlos Barbosa","Operation":"UPT","distance":"15","to_id":"501"}" http://localhost:3333/router/city  
  ```

**Removing a city**
To remove a city we must use the operation **DEL**

  ```
  curl -H "Content-Type: application/json" -X POST -d "{"id":"503", "Operation":"DEL"}" http://localhost:3333/router/city  
  ```

**Checking the health of the system**
To check if all microservices are running, we can access the following endpoint:

  ```
  curl http://localhost:3333/router/health  
  ```

The value 200 as answer indicates that everything is OK.

**Checking the shortest path from one city to another**

To check which rote is the shortest one between one city and another, we must access the following link:

  ```
  curl http://localhost:3333/router/city/shortest/{id_origin_city}/to/{id_destination_city}  
  ```

To show a complete example let's run the following commands to populate our city database:

  ```
  curl -H "Content-Type: application/json" -X POST -d "{"id":"601","name":"City 1","Operation":"ADD"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"602","name":"City 2","Operation":"ADD","distance":"10","to_id":"601"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"603","name":"City 3","Operation":"ADD","distance":"5","to_id":"601"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"604","name":"City 4","Operation":"ADD","distance":"3","to_id":"602"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"605","name":"City 5","Operation":"ADD","distance":"8","to_id":"602"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"605","Operation":"ADD","distance":"4","to_id":"603"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"606","name":"City 6","Operation":"ADD","distance":"2","to_id":"604"}" http://localhost:3333/router/city  
  curl -H "Content-Type: application/json" -X POST -d "{"id":"606","Operation":"ADD","distance":"1","to_id":"605"}" http://localhost:3333/router/city  
  ```

And now let's check the shortest path between the City 2 and the City 3:

  ```
  curl http://localhost:3333/router/city/shortest/602/to/603  
  ```

We will have the following response:

  ```
  [{"From":"City 2"},{"To":"City 4"},{"To":"City 6"},{"To":"City 5"},{"To":"City 3"},{"Total":"10"}]  
  ```

## Ideas for improvements in future developments

* For searching the shortest route we can mix Dijkstra algorithm, or any other algorithm to solve this problem, with a pre processed data approach. The shortest routes could be processed in defined intervals of time. For example, detecting that cities and distances were included or modified, a process could be launched to recalculate the distances between each city. So, having a control of the time of  last city data update and the time of last run of the pre process algorithm, it is possible to determine if there is need to calculate distances during runtime or not.

* Each microservice could be deployed in a server on AWS.

* The process of deploy could be automatized when a merge to the master branch is performed, by using a continuous integration and delivery tool. AWS Code Pipeline could be applied for this purpose. 

* H2 database must be replaced by a database for production environments.

* Tests for rest communication must be implemented.

* Stress tests could be implemented.
