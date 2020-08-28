package com.github.mahendragohel;



import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.Swagger;
import io.swagger.models.properties.*;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Mojo(name = "generate-schema", defaultPhase = LifecyclePhase.COMPILE)
public class MyMojo extends AbstractMojo {
    @Parameter
    private String inputYamlFileName;

    @Parameter
    private String modelPackage;

    @Parameter
    private String sourceFolder;

    @Parameter
    private String inputDirectory;

    @Parameter
    private File outputDirectory;

    @Parameter(defaultValue = "${project.build.directory}")
    private String projectBuildDir;

    @Parameter(defaultValue = "${project}")
    private MavenProject mavenProject;

    @Parameter
    private String clientKitPackageName;

    private static final String GRAPHQL_EXTENSION = ".graphqls";
    private static final String INTERFACE = "interface";
    private static final String TYPE = "type";
    private static final String MUSTACHE_FILE = "graphqlSchema.mustache";

    private static List<String> customScalars = new ArrayList<>();
    private final static List<String> graphqlDefaultScalars = new ArrayList<>(Arrays.asList("String", "Integer", "Int", "Float","Boolean", "ID"));

    public void execute() throws MojoExecutionException {

        try (Stream<Path> paths = Files.walk(Paths.get(mavenProject.getBasedir() + "/" + inputDirectory))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        getLog().info("generating graphql schema from yaml file");
        getLog().info(inputYamlFileName + "==" + modelPackage + "==" + sourceFolder + "==" + inputDirectory + "==" + outputDirectory);

        String fileName = inputYamlFileName.replaceFirst("[.][^.]+$", GRAPHQL_EXTENSION);


        File f = outputDirectory;
        if (!f.exists()) {
            f.mkdirs();
        }

        try {
            getLog().info("Input directory: " + inputDirectory + inputYamlFileName);
            getLog().info(mavenProject.getBasedir().toString());
            SwaggerParser swaggerParser = new SwaggerParser();
            Swagger swagger = swaggerParser.read(mavenProject.getBasedir() + "/" + inputDirectory + inputYamlFileName);
            getLog().info("Read swagger file");
            Map<String, Model> definitions = swagger.getDefinitions();
            GraphQLSchema graphQLSchema = new GraphQLSchema();
            List<GraphQLModel> graphQLModels = new ArrayList<>();

            List<String> extendedModelNames = new ArrayList<>();

            definitions.entrySet().forEach(def -> {
                GraphQLModel graphQLModel = new GraphQLModel();
                List<GraphqlField> graphqlFields = new ArrayList<>();

                Model model = def.getValue();
                graphQLModel.setModelName(def.getKey());
                if(model instanceof ComposedModel){
                    List<Model> models = ((ComposedModel) model).getAllOf();
                    models.stream().forEach(mod -> {
                        if(mod instanceof RefModel){
                            extendedModelNames.add(((RefModel) mod).getSimpleRef());
                            graphQLModel.setExtendingInterface(((RefModel) mod).getSimpleRef());
                            graphQLModel.setExtends(true);
                            Model referenceModel = definitions.get(((RefModel) mod).getSimpleRef());
                            if(null != referenceModel){
                                Map<String, Property> properties = referenceModel.getProperties();
                                if (null != properties) {
                                    getGraphqlFields(properties, graphqlFields);
                                }
                            }
                        }
                        Map<String, Property> properties = mod.getProperties();
                        if (null != properties) {
                            getGraphqlFields(properties, graphqlFields);
                        }
                    });
                } else {
                    Map<String, Property> properties = model.getProperties();
                    if (null != properties) {
                        getGraphqlFields(properties, graphqlFields);
                    }
                }
                graphQLModel.setGraphqlFields(graphqlFields);
                graphQLModels.add(graphQLModel);
            });

            graphQLModels.stream().forEach(graphQLModel -> {
                if(extendedModelNames.contains(graphQLModel.getModelName())){
                    graphQLModel.setModelType(INTERFACE);
                } else {
                    graphQLModel.setModelType(TYPE);
                }
            });
            List<CustomScalar> customScalarList = customScalars.stream().map(s -> {
                return new CustomScalar(s);
            }).collect(Collectors.toList());
            graphQLSchema.setGraphqlModel(graphQLModels);
            graphQLSchema.setCustomScalars(customScalarList);

            String content = mustacheContent(graphQLSchema, MUSTACHE_FILE);
            generateFile(fileName, content);

            getLog().info("generated schema file");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getGraphqlFields(Map<String, Property> properties, List<GraphqlField> graphqlFields){
        properties.entrySet().forEach(property -> {
            String type = "";
            String description = "";
            String name = property.getKey();
            boolean required = false;
            if(property.getValue() instanceof BaseIntegerProperty){
                BaseIntegerProperty baseIntegerProperty = (BaseIntegerProperty) property.getValue();
                type = baseIntegerProperty.getType();
                description = baseIntegerProperty.getDescription();
                required = baseIntegerProperty.getRequired();
            }
            if(property.getValue() instanceof IntegerProperty){
                IntegerProperty integerProperty = (IntegerProperty) property.getValue();
                type = integerProperty.getType();
                description = integerProperty.getDescription();
                required = integerProperty.getRequired();
            }
            if(property.getValue() instanceof ObjectProperty){
                ObjectProperty objectProperty = (ObjectProperty) property.getValue();
                type = "Json";
                if(!graphqlDefaultScalars.contains(StringUtils.capitalize(type)) && !customScalars.contains(StringUtils.capitalize(type))){
                    customScalars.add(type);
                }
                description = objectProperty.getDescription();
                required = objectProperty.getRequired();
            }
            if(property.getValue() instanceof BooleanProperty){
                BooleanProperty booleanProperty = (BooleanProperty) property.getValue();
                type = booleanProperty.getType();
                description = booleanProperty.getDescription();
                required = booleanProperty.getRequired();
            }
            if(property.getValue() instanceof MapProperty){
                MapProperty mapProperty = (MapProperty) property.getValue();
                type = "Json";
                if(!graphqlDefaultScalars.contains(StringUtils.capitalize(type)) && !customScalars.contains(StringUtils.capitalize(type))){
                    customScalars.add(type);
                }
                description = mapProperty.getDescription();
                required = mapProperty.getRequired();
            }
            if(property.getValue() instanceof FloatProperty){
                FloatProperty floatProperty = (FloatProperty)property.getValue();
                type = floatProperty.getFormat();
                description = floatProperty.getDescription();
                required = floatProperty.getRequired();
            }
            if(property.getValue() instanceof DateTimeProperty){
                DateTimeProperty dateTimeProperty = (DateTimeProperty) property.getValue();
                type = dateTimeProperty.getType();
                if(!graphqlDefaultScalars.contains(StringUtils.capitalize(type)) && !customScalars.contains(StringUtils.capitalize(type))){
                    customScalars.add(type);
                }
                description = dateTimeProperty.getDescription();
                required = dateTimeProperty.getRequired();
            }
            if (property.getValue() instanceof StringProperty) {
                StringProperty stringProperty = (StringProperty) property.getValue();
                type = stringProperty.getType();
                description = stringProperty.getDescription();
                required = stringProperty.getRequired();
            }
            if (property.getValue() instanceof RefProperty) {
                RefProperty refProperty = (RefProperty) property.getValue();
                type = refProperty.getSimpleRef();
                description = refProperty.getDescription();
                required = refProperty.getRequired();
            }
            if (property.getValue() instanceof ArrayProperty) {
                ArrayProperty arrayProperty = (ArrayProperty) property.getValue();
                if (arrayProperty.getItems() instanceof StringProperty) {
                    StringProperty stringProperty = (StringProperty) arrayProperty.getItems();
                    if(!graphqlDefaultScalars.contains(StringUtils.capitalize(stringProperty.getType())) && !customScalars.contains(StringUtils.capitalize(stringProperty.getType()))){
                        customScalars.add(StringUtils.capitalize(stringProperty.getType()));
                    }
                    type = "[" + StringUtils.capitalize(stringProperty.getType()) + "]";
                    description = stringProperty.getDescription();
                    required = stringProperty.getRequired();
                }
                if (arrayProperty.getItems() instanceof RefProperty) {
                    RefProperty refProperty = (RefProperty) arrayProperty.getItems();
                    type = "[" + StringUtils.capitalize(refProperty.getSimpleRef())+ "]";
                    description = refProperty.getDescription();
                    required = refProperty.getRequired();
                }
            }
            if (required) {
                type = type + "!";
            }
            graphqlFields.add(new GraphqlField(description, name.replace("@", ""), StringUtils.capitalize(type)));
        });
    }

    public static String mustacheContent(GraphQLSchema context, String template) {
        try {
            StringWriter writer = new StringWriter();
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(template);
            mustache.execute(writer, context).flush();
            return writer.toString();
        } catch (Exception ex) {
        }
        return "";
    }

    private void generateFile(String fileName, String content) {
        try {
            File f = outputDirectory;
            if (!f.exists()) {
                f.mkdirs();
            }
            File schemaFile = new File(f, fileName);
            FileWriter w = null;
            w = new FileWriter(schemaFile);
            w.write(content);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
