# Yaml to GraphQL Schema Generator 
#### graphql-java-schema-generator

You can  pull **_graphql-java-schema-generator_** from the central maven repository:
```xml
<dependency>
  <groupId>com.github.mahendragohel</groupId>
  <artifactId>graphql-schema-generator</artifactId>
  <version>1.4.2</version>
</dependency>
```

Just add the below code to your projects POM.xml file to generate the graphql schema file.
```xml                   
<plugin>
     <groupId>com.github.mahendragohel</groupId>
     <artifactId>graphql-schema-generator</artifactId>
     <version>1.4.1</version>
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
            <!-- whether to replace the special character or not by default if will remove special character -->
         <replaceSpecialCharacter>true</replaceSpecialCharacter>
         <replacementCharacter>_</replacementCharacter>
     </configuration>
 </plugin>
```
if you don't specify below config parameter then it will generate schema under:
> `${project.build.directory}/generated-sources/graphql`

    <outputDirectory>src/main/resources/graphql</outputDirectory>
    
Below configuration will replace any special character present in the field property of the yaml definition model.

             <replaceSpecialCharacter>true</replaceSpecialCharacter>
             <replacementCharacter>_</replacementCharacter>

For example,
if you have field with below property then it will remove @ from property as graphql does not support special characters.
```yaml
    '@type':
        type: string
        description: 'some description'
```             

Run below command to generate .graphqls schema files from yaml:

    mvn clean install
 ## Support
 If you need help using **_graphql-java-schema-generator_** feel free to drop an email or create an issue in github.com (preferred)
 
 ## Contributions 
 * Provide suggestion/feedback/Issue
 * pull requests for new features
 * Star :star2: the project