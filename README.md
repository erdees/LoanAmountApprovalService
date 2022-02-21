# Loan Amount Approval Service
This is an example of a loan amount approval service which was made as a homework to show my coding skills.
The task completed according to the task.txt which you may find in this repo as well. 
 
## Build and run 

### Docker and docker-compose

You can run the service in containers. 

#### Prerequisites

-git \
-Docker \
-docker-compose 

#### Installation

Clone the repo:

```
git clone git@github.com:erdees/LoanAmountApprovalService.git
```

cd to a target directory, where service code was cloned: 

````
cd LoanAmountApprovalService
````

And run container:

```
docker-compose up -d
``` 

Build will be done automatically, once container will up and running, service will be ready. 
By default, an application listening port 8080, so it should be available on `http://localhost:8080`
 
### Build and start without containers

#### Prerequisites

-java-1.8.0-openjdk or oracle Java 8 installed (both tested); \
-Graddle 7.3+ installed or use graddle wrapper included in distro \
-git installed; (yum install git on CentOS 7-8)

#### Installation 

Further installation process described for a Linux/Mac machine. For other types of OS it may be different. 

Clone the repo:

```
git clone git@github.com:erdees/LoanAmountApprovalService.git
```

cd to a target directory, where service code was cloned: 

````
cd LoanAmountApprovalService
````

Initiate application build:

````
graddle build
````

Once it finished, the java artifact will be located at build/libs folder named `approvalservice-0.0.1-SNAPSHOT.jar`

If you want tu run it, type a command at the same folder: 

````
java -jar approvalservice-0.0.1-SNAPSHOT.jar
````

Once done, application is up and running.

##### Setting up as a linux Systemd service (rhel example)

If you want to automate application startup, you may use a .service file 
from distro. 

To do it, we need to create systemd .service file which you can copy and configure from root repository folder:

`cp Approval.service /etc/systemd/system/`

Edit required parameters in the .service file.

`vim  /etc/systemd/system/Approval.service`

Change the app working directory where .jar file located, for example: 

`WorkingDirectory=/opt/apps/ApprovalService`

Once done, check java parameters with required quantity of RAM for working application. 

`ExecStart=/bin/java -Xms128m -Xmx128m -jar approvalservice-0.0.1-SNAPSHOT.jar`

If you have changed anything in .service file, please reload a configuration by using following command:

`systemctl daemon-reload`

To add the app to autostart, use the command:

`systemctl enable Approval.service`

and finally, to start the app:

`systemctl start Approval.service`

By default, an application listening port 8080, so it should be available on `http://localhost:8080`
Now the app up and running, you are beautiful.

## Distro description

graddle - graddle wrapper which may used on a machine without graddle installed \
src - a main source code folder \
src/integration - api/integration tests for an app \
src/main - service source code \
src/test - unit tests \
Approval.service - systemd service file for linux \
build.gradle - main graddle build file \
docker-compose.yml - docker compose file for Docker container management \
Dockerfile - settings for docker container building \
gradlew - script for running a graddle wrapper on a machine without graddle installed \
settings.graddle - graddle settings file \
task.txt - an original task 

## Application description 

ApprovalService is a RESTful service that allows loan preparators to send loan contracts to loan managers for approval. 
Loan preparator must specify the customer’s ID and the amount that customer wants to loan. 
Loan preparator must also specify which managers need to approve it . After the specified managers have added 
their approvals the contract will be automatically sent to the customer.

### Endpoint description

According to original task, an application has three endpoinds:
1) /api/loan/request (POST) \
Where loan preparators should create a request for loan approval. 
The request should have the following format:

````
{
  "customerId": "21-Q2A1-XX9",
  "loanAmount": 1000,
  "approvers": ["Mamertas Juronis", "Gaile Minderyte", "Nina Nugariene"]
}
````

Where:

customerId (String) should be in XX-XXXX-XXX pattern, where X is either number or a letter; \
loanAmount (numeric) should be a number; \
approvers (array of strings) should be a list of loan managers who will approve or decline the request. 
Max. 3 persons allowed. 

When a loan preparator will send a correct request (basic checks on a request format will take a place), the system 
will put the request to a queue where it can be processed by a loan manager.
While the customer with ID has a pending loan, new requests can't be performed. To send a new request, 
a current one should be approved or declined.  

2) /api/loan/approval (POST) \
Where a loan manager should take a decision on a loan. 
The request should have the following format:

````
{
  "customerId": "21-Q2A1-XX9",
  "loanApprover": "Mamertas Juronis",
  "approved": true
}
````

Where:

customerId (string) should be an ID of a customer with a pending loan. \
loanApprover (string) should be one of three or fewer approvers specified during initial request. \
approved (boolean) decision on a loan. If `false` chosen, the contract will not be sent to the customer and will not 
appear in a report. All `true` or approved loans considered to be sent to the customer and appear in statistics. 

3) /api/loan/report (GET) \
Report which shows statistics on approved loans during configured period (can be change in a configuration file). 
Endpoint don't have any parameters or request body.
