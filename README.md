# Annotation Processor
A very basic annotation processor that auto-generates Java code at compile time. It defines a **Factory** annotation type which can be used as follows:
```java
@Factory(type = Meal.class, id = "Pizza")
class Pizza implements Meal {}
```
The generated code lives in a file named ```<@Factory.type()>Factory.java``` (```MealFactory.java``` in the above case).
## Prerequisites:
- [Maven](https://maven.apache.org/) should be installed
- Atleast version 8 of [Java](https://www.java.com/en/download/)
## Usage
- Clone and `cd` into this repo:

  `git clone https://github.com/ongebo/annotation-processor.git`

  `cd annotation-processor`
- Package the project:

  `mvn package`

- Install the build artifact into your local maven repository:

  `mvn install`

- Test the processor with the sample project in [samples/pizzastore](samples/pizzastore)
