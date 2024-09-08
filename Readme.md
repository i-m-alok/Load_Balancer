# Load Balancer

## Part 1:
- [BackendServer.java](BackendServer.java) contains a basic server code, you can consider it as a independent java program which can accept the port from the user and rolls up a server.
- To compile the program ``javac BackendServer.java`` 
- To execute it ``java BackendServer <port>``

## Part 2:
- Wrote a [Round Robin Algorithm](./src/com/loadbalancer/balancing_strategy/RoundRobinStrategy.java) to distribute the request among servers

## Part 3:
- Wrote a [LBHttpHandler](./src/com/loadbalancer/LBHttpHandler.java) where balancing strategy is integrated and distributed the request directly to server

## Part 4:
- Implemented a health check mechanism to see if server is healthy or not and Implemented the ``Configuration`` class using Singleton pattern and read about Multi-threading
- Enhanced ``LBHttpHandler`` to route the traffic if and only if the server suggested by Round Robin Algorithm is healthy. [link](./src/com/loadbalancer/LBHttpHandler.java)

## Part 5:
- Refactoring :P



# Learnings:
- **Volatile** provides the visibility of change among all threads but does not guarantee about atomicity of update. So, to handle this we need to use the **synchronized** keyword or use lock while doing the update, to prevent the race conditions.
- **AtomicInteger** ensure the atomic operations for integer values, it allow us to update value without needing the explicit synchronization.

# Funny Moments
- After trying multiple times I could not able to work with log4j :(

# Resources:
- https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html
- https://www.codeproject.com/Tips/1040097/Create-a-Simple-Web-Server-in-Java-HTTP-Server
- https://codingchallenges.fyi/challenges/challenge-load-balancer/
- https://refactoring.guru/design-patterns/singleton/java/example#example-2


# Try running this code:
- Please start the server using the step one (i could have ignored class files)
- While starting the LBServer pass running server links with space seperated.
- Try sending request to LB and see if it distributes the traffic or not

# Possible Enhancements
- Read about other load balancing algorithms and see if I can implement
- Refactor the current interface to support any type of load balancing algorithm
- Try converting using async framework