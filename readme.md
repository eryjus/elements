Century Business Applications (cba) Element Maintenance
---

The cba Element Maintenance application is used as a basic input of the fundamental data elements required to get cba running.  The Elements in question related to the elements of business data that will correspond to the database columns, along with some business description of how to use them.


**Build System**

I am using Maven2 as my build system.  The project should build properly on JDK 1.8, and should run on JRE 1.8 as well.  I would highly recommend the Oracle version since it will be packaged with JavaFX.  See https://java.com for more details.  OpenJDK may or may not include the JavaFX runtime libraries and I am not testing on OpenJDK.  Other than that, if you have not used Maven before, it will automatically download all the dependencies required to execute the application and will locate them in the same target folder as the application.  Use `mvn package` to compile and package the application for execution.  Then, use `java -jar target/elements-0.1.0-SNAPSHOT.jar` to run the program.  Check the version carefully -- it will be on the .jar file in the target folder.


**Development Environment**

I use vscode for my code editor.  I also use Oracle's SceneBuilder in standalone mode as a GUI development platform for the JavaFXML files.  Instructions for installation are found here: https://docs.oracle.com/javase/8/scene-builder-2/installation-guide/jfxsb-installation_2_0.htm.  The same web site has information for integrating SceneBuilder into Eclipse or NetBeans.  I am developing and testing on Fedora Core 26.