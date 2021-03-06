# Dream-Car-Project
This project is implemented to meet the requirements of J2ee course which is a subject in MSc. Database Administartion.

# Technologies used
  - Bootstrap
  - Thymeleaf
  - Java validation
  - J2ee
  - Spring Boot
  - Hibernate
  - MySQL
  
  
# Introduction
Dreamcar is a successful car manufacturer that plans to expand into new markets in the coming years. To achieve this goal, the company identified that it would save up to 30% of costs if standard components (tires, suspensions, brake pads, etc.) were purchased using reverse auctions open to any international partner, instead of relying only on on local suppliers. Reverse auction means that Dreamcar is requesting a specified quantity of a component (eg 400,000 tires) for purchase, and any international supplier participating in the auction may bid (for example, Heavenprovider can deliver the tires for $ 65). per piece). We will consider that a supplier can see the offers of the other suppliers, so he can later modify his offer in order to be competitive. The auction is closed and Dreamcar selects a winner when a stop criterion is reached. The stopping criteria set by Dreamcar are: either the target price (eg: when the price of a tire becomes less than or equal to $ 50), or timeout (eg: 1 week after the launch of the auction). When a stopping criterion is reached, the auction is considered closed and the winner is notified (eg by email, SMS). The application must provide users with secure access by using a username / password.

# System Definition
DreamCar is a system that allows the customer (DreamCar) and the supplier to login to the system to benefit of its services. The customer can create a request for bids to supply parts or components such as as wheels, tires, suspension, brake discs, and pads using a reverse auctioneering with multiple potential suppliers, then the competition between suppliers starts and each supplier can submit a bid for this auction. It is required to design and implement a Java EE application that meets the requirements of Dreamcar.

# How to run
This project implemented on IntelliJ IDEA.

First Clone this repo, and open the cloned repo by IntelliJ IDEA.  
Then you have to setup you database by creating MySQL database and then in the application.properties file you can add your database details to link it with the project, then you can run the project without any problem.  
If you want to use email service you will find the source code in the directory ***config*** file name EmailServiceImpl, inside this file you have to replace tags with the sender and its password so this service work proprely.
