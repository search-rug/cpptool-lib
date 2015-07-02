# cpptool-lib

A java library for reading the data files produced by [CppTool](https://github.com/search-rug/cpptool).

This project uses Java 8 and contains a public api and the implementation.
Users should only use the classes contained in `nl.rug.search.cpptool.api`.
See [the example Main.java](src/main/java/Main.java) for an example on how to use the library.

#### Building
* Clone this project and run `git submodule update --init`.
* Execute the command `./gradlew jar` to produce a minimal jar 
* Execute the command `./gradlew shadowJar` to produce a jar with all dependencies included.

This project is MIT licensed.
