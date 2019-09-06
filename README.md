

# Web Server (Apache)
Basic web framework able to handle petitions such as html, images and methods defined in POJOs.

## Getting Started
Follow the next instructions to run an deploy the project 
### Prerequisites

 - Maven 3.6. If you don't have it, follow this tutorial according to your OS. [How to install maven](https://maven.apache.org/install.html)
 - JDK (Java Development Kit) 1.8. If you don't have it, go to this tutorial. [Java SE Development Kit 8](https://www.oracle.com/java/technologies/jdk8-downloads.html)
 - Git bash. If you don't have it, go here [GIT](https://git-scm.com/) and follow this basic turorial. [GIT DOCS](https://git-scm.com/docs)

### Installation and Local Deployment
Clone the project:

    git clone https://github.com/darm145/arepProject

use cd to navigate through the folders and access the project

    cd arepProject

Run it

    mvn package
    mvn exec:java -D exec.mainClass="edu.escuelaing.arep.AppServer"
    
### Web Deployment
This project is deployed under Heroku

[![Servidor Apache](https://www.herokucdn.com/deploy/button.png)](http://arep-project-david-ramirez.herokuapp.com/hola.html)

### Use of the application 
to obtain images:

    http://arep-project-david-ramirez.herokuapp.com/Leon.jpg
to obtain .html:

    http://arep-project-david-ramirez.herokuapp.com/hola.html
   
 to access POJOs 
 
	 http://arep-project-david-ramirez.herokuapp.com/app/POJO/saludar?name=DanielB
	
    


## Built with
[Maven](https://maven.apache.org/) - Dependency Management

## Author
David Daniel A. Ramirez Moreno- [GitHub](https://github.com/darm145) - Escuela Colombiana de Ingeniería Julio Garavito

## License
This project is under GNU General Public License - see  [LICENSE](https://github.com/darm145/arepProject/blob/master/LICENSE) to more info.
