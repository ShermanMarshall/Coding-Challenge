# Coding-Challenge

The challege was completed using Java, depending primarily upon Maven, and Spring Boot/Tomcat to facilitate HTTP connections.

The source code is run using a script (run.sh) in the root folder along with the pom.xml and src/ folder containing the code. 

The script has two environment variables (ETC_PASSWD & ETC_GROUP) which may be adjusted to the absolute paths of the files on the system; however, in order to test that they function properly, the files are copied locally, the paths are exported into the environment as system variables, and they are then made accessible to the application. This aids in testing.

A separate script (test.sh) is included for testing purposes. It functions much the same as the run.sh script, but for testing purposes. Most of the test output of concern is visible in src/test/resources in json format.

