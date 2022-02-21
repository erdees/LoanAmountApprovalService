# Loan Amount Approval Service
This is an example of a loan amount approval service which was made as a homework to show my coding skills.
The task was done according to the task.txt which you may find in this repo as well. 
 
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

Change the bot working directory where .jar file located, for example: 

`WorkingDirectory=/opt/apps/ApprovalService`

Once done, check java parameters with required quantity of RAM for working application. 

`ExecStart=/bin/java -Xms128m -Xmx128m -jar approvalservice-0.0.1-SNAPSHOT.jar`

If you have changed anything in .service file, please reload a configuration by using following command:

`systemctl daemon-reload`

To add the bot to autostart, use the command:

`systemctl enable Approval.service`

and finally, to start the bot:

`systemctl start Approval.service`

Now the bot up and running, you are beautiful.

