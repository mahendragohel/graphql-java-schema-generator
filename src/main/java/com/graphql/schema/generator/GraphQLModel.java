package com.graphql.schema.generator;

import java.util.List;

public class GraphQLModel {
    private String modelName;
    private String modelType;
    private List<GraphqlField> graphqlFields;
    private boolean isExtends = false;
    private String extendingInterface;

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public boolean isExtends() {
        return isExtends;
    }

    public void setExtends(boolean anExtends) {
        isExtends = anExtends;
    }

    public String getExtendingInterface() {
        return extendingInterface;
    }

    public void setExtendingInterface(String extendingInterface) {
        this.extendingInterface = extendingInterface;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<GraphqlField> getGraphqlFields() {
        return graphqlFields;
    }

    public void setGraphqlFields(List<GraphqlField> graphqlFields) {
        this.graphqlFields = graphqlFields;
    }
}
