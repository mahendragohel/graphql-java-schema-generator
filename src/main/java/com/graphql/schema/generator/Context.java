package com.graphql.schema.generator;

import java.util.List;

public class Context {
    String classname;
    String packages;
    List<GraphqlField> graphqlFields;
    String clientKitPackageName;

    public String getClientKitPackageName() {
        return clientKitPackageName;
    }

    public void setClientKitPackageName(String clientKitPackageName) {
        this.clientKitPackageName = clientKitPackageName;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<GraphqlField> getGraphqlFields() {
        return graphqlFields;
    }

    public void setGraphqlFields(List<GraphqlField> graphqlFields) {
        this.graphqlFields = graphqlFields;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public static class GraphqlField {
        String methodReturnType;
        String graphQlfieldName;
        String fieldName;
        String classname;

        public GraphqlField(String methodReturnType, String graphQlfieldName, String fieldName, String classname) {
            this.methodReturnType = methodReturnType;
            this.graphQlfieldName = graphQlfieldName;
            this.fieldName = fieldName;
            this.classname = classname;
        }

        public String getMethodReturnType() {
            return methodReturnType;
        }

        public void setMethodReturnType(String methodReturnType) {
            this.methodReturnType = methodReturnType;
        }

        public String getGraphQlfieldName() {
            return graphQlfieldName;
        }

        public void setGraphQlfieldName(String graphQlfieldName) {
            this.graphQlfieldName = graphQlfieldName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }


        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }
    }
}
