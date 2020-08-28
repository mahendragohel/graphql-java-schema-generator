#Yaml to GraphQL Schema Generator 
graphql-java-schema-generator

You can  pull graphql-java-schema-generator from the central maven repository:
```xml
<dependency>
  <groupId>com.github.mahendragohel</groupId>
  <artifactId>graphql-schema-generator</artifactId>
  <version>1.3</version>
</dependency>
```

Just add the below code to your projects POM.xml file to generate the graphql schema file.
```xml                   
<plugin>
     <groupId>com.github.mahendragohel</groupId>
     <artifactId>graphql-schema-generator</artifactId>
     <version>1.0-SNAPSHOT</version>
     <executions>
         <execution>
             <goals>
                 <goal>generate-schema</goal>
             </goals>
         </execution>
     </executions>
     <configuration>
            <!--it will read all .yaml file from below inputDirectory config location-->
         <inputDirectory>src/main/resources/swagger/</inputDirectory>
            <!--it will generate .graphql files at below outputDirectory config location-->
         <outputDirectory>src/main/resources/graphql</outputDirectory>
     </configuration>
 </plugin>
```
if you don't specify below config parameter then it will generate schema under `${project.build.directory}/generated-sources/graphql` :

    <outputDirectory>src/main/resources/graphql</outputDirectory>
    
Run below command to generate .graphqls schema files from yaml:

    mvn clean install
 ## Support
 If you need help using graphql-java-schema-generator feel free to drop an email or create an issue in github.com (preferred)
 
 ## Contributions 
 * Provide suggestion/feedback/Issue
 * pull requests for new features
 * Star :star2: the project