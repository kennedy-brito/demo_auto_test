# Running tests with Maven
### Configuration

- surefire
    ````xml
    <!-- UNIT TEST-->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
            <skip>${skipUnitTests}</skip>
        </configuration>
    </plugin>
    ````
- failsafe
    ````xml
    <!-- INTEGRATION TEST-->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
        <executions>
            <execution>
                <id>integration-tests</id>
                <goals>
                    <goal>integration-test</goal>
                    <goal>verify</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ````
  

### Commands
- Run all tests ``.\mvnw clean verify``

- Run unit tests and skip IT test ``.\mvnw clean verify -DskipITs`` | `.\mvnw clean test`

- Run IT tests and skip unit test ``.\mvnw clean verify -DskipUnitTests=true``

# Code Coverage
### Configuration
- Jacoco
````xml
<!-- Code Coverage -->
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.12</version>
  <!--
  If you want to exclude some file from the analyses 
  you should configure it using the exclude tag
  -->
  <configuration>
    <excludes>
      <!--file path: 
      *   Match zero or more characters
      **  Match zero or more directories
      ?   Match a single character-->
      <exclude>com/kennedy/demo_auto_test/DemoAutoTestApplication.class</exclude>

    </excludes>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>prepare-agent</goal>
      </goals>
    </execution>
  <execution>
    <id>report</id>
    <phase>prepare-package</phase>
    <goals>
      <goal>report</goal>
    </goals>
  </execution>
</plugin>
````

### Run test and generate report
``.\mvnw clean test jacoco:report``

### How to exclude a method from the report
Sometimes a method is not used during the execution of a app, it may be for debugging purposes like the toString

To exclude the method from the analysis you can create the annotation and use it in the method you want to use
````java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcludeFromJacocoGeneratedReport {
}
````