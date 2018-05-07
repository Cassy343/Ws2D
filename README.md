# Ws2D
The purpose of this utility is to provide a simple object-oriented way to create a web-game with a Java back-end. Ws2D (Web Service 2D) is a platform on which a 2D
game can be built, removing the hassle of networking and physics implementation. Another benefit of Ws2D is that all libraries are managed by the Jar. When launched,
Ws2D will unpack any libraries that it cannot find automatically.
<br/>
Please note that this platform is currently still in development and is not ready for release.

# Project Setup
The Ws2D Jar has project setup integrated into it. Use the following steps to create a project:
- Download or build the Ws2D Jar. The Jar can be invoked like any other: `java -jar <jarFile>`
- Run the Ws2D Jar file with the following arguments: `setup <rootDir> <mainClass> <projectName> <ws2dJar> <bash|batch>`
	- `rootDir`: The project root directory.
	- `mainClass`: The main class of your project. This should be a full class name, for example: `com.test.main.MyGame`
	- `projectName`: The name of your project.
	- `ws2dJar`: The Ws2D Jar file you plan to use for your project.
	- `bash|batch`: You must choose to have the project setup generate either bash or batch scripts depending on your OS or preference. A build and launch script will
	be generated in the language you choose.
- You may configure your IDE on the following setup. Be sure to include a reference to the Ws2D Jar as a library.
	- If you would like to have your IDE build the game Jar, have a reference to the Ws2D Jar in the manifest.

# Launching the Project
- Use the generated launch script in the run folder to run the project. The game Jar and Ws2D jar should be in that directory.
- The necessary libraries for Ws2D will be extracted at runtime if they are not located.
- Settings for the hosted HTTP service can be specified in the `game.json` file of your project.