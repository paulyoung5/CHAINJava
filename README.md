<p>
  <img src="https://user-images.githubusercontent.com/6493590/38195471-7ff6083c-3675-11e8-9c05-c07d5bc094fb.png" alt="CHAIn: Combining Heterogeneous Agencies' Information" />
</p>

[![Build Status](https://travis-ci.org/lewis785/CHAINJava.svg?branch=master)](https://travis-ci.org/lewis785/CHAINJava)

The [CHAIn (Combining Heterogeneous Agencies’ Information) system](https://researchportal.hw.ac.uk/en/publications/dynamic-data-sharing-for-facilitating-communication-during-emerge) dynamically re-writes queries to databases when mismatches led to query failure. This repository focuses on a new SQL component.

# User Guide

The CHAIN system is currently available as a Java Library and can be used in other java projcets.  The system is not yet availiable on a public repository, but uberjar releases (containing all dependencies) are available [here](https://github.com/lewis785/CHAINJava/releases).

The following code shows how to use the system once the library is imported:

```java
package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import chain.sql.ChainDataSourceException;
import chain.sql.SQLAdapter;


public class MainClass {
	public static void main(String[] args) {
		try {
			SQLAdapter adapter = new SQLAdapter("jdbc:mysql://mysql-server-1.macs.hw.ac.uk/dac31", "dac31", "sql");
			
			ResultSet results = adapter.executeQuery("SELECT surname FROM users");
			
			if(results.next()) {
				System.out.println(results.getString(1));
			}
			
		} catch (ChainDataSourceException e) {
			// Fail during chain process
			e.printStackTrace();
		} catch (SQLException e) {
			// Fail when parsing results
			e.printStackTrace();
		}
	}
}

```
Assume that the database table `users` did not actually contain a collumn `lastname`, but instead used `surname`.  

The code above would try and run the query and notice that it fails.  It would then repair the query and run it again, this time retrieveing the correct results.

# Developer
For further information about the system's architecture and underlying technologies, see the [Wiki](https://github.com/lewis785/CHAINJava/wiki).

## Getting Started
Begin by cloning the repository by running the following in your terminal:

```
$ git clone https://github.com/lewis785/CHAINJava
$ cd CHAINJava
```

You can then install dependencies needed by CHAIn:

```
$ ./gradlew getSPSM
```

If you have any issues, check the [Wiki](https://github.com/lewis785/CHAINJava/wiki) or the troubleshooting guide at the bottom of this README file.


## Gradle
The project uses Gradle as its build tool and for dependency management.  

#### Dependencies

While most dependencies are available on a 
public repository, SPSM is not.  A gradle task will execute a script to retrieve them and build them from github sources.

The following task will do this (though it will be automatically done if the project is built and it is not yet 
installed):

```
$ ./gradlew getSPSM
```

#### Testing

The tests can be executed using gradle.

|  Command                    |      Description            |
|-----------------------------|-----------------------------|
| `./gradlew integrationTest` |  Runs the integration tests |
| `./gradlew sparqlTest`      |  Runs the sparql test suite |
| `./gradlew test`            |  Runs unit tests            |

There are three test sets: integrationTest, sparqlTest and the unit tests. A test report will be available at a link provided by the console.

#### Building

The project can be built from gradle.  This is done using:

```
$ ./gradlew build
```

This will build everything and run the tests.  The tests can be ignored by appending `-x test` and/or `-x fastTest`.
All of the built items will available in the build directory (the jar file will be in build/libs).

### Importing into Eclipse

To import into eclipse:
 
- From the `file`  menu, choose`import`.
- Select `Gradle` then `Gradle Project`.
- Press `Next`, choose the location of the project, then press `Finish`.

The project will now be imported, but will show errors if the `getSPSM` task has not been run.  To fix this in eclipse:

- From the `Gradle Tasks` pane, run `build->getSPSM` and wait of the task to finish.
- In the folder pane, right click on the `build.gradle` file and select `gradle` then `refresh gradle project` - 
this will cause eclipse to refresh the project, this time finding the SPSM dependencies.

### Importing into Intellij

To import into intellij

- Close the current project and select `import`
- Find the location of the project and select the `build.gradle` file

The project should now be imported.  Hover over the icon in the bottom left of the screen and select `Gradle` to view the 
Gradle pane.  If there are errors in the project it will be because SPSM has not been installed yet.

To fix this, run the `getSPSM` task by clicking `Tasks` -> `build` -> `getSPSM` in the Gradle pane.

## Troubleshooting

*  [Maven](https://maven.apache.org/install.html) must be installed for the project to build. If you're a Mac user with [Homebrew](https://brew.sh) installed, you can use the `brew install maven` command.
*  The project will not run on Java version 9; please ensure you have installed Java **version 8**. To check this, try `java -version`. 
