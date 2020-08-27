package com.github.mahendragohel;

import java.util.List;

public class GraphQLSchema {
    private List<CustomScalar> customScalars;
    private List<GraphQLModel> graphqlModel;

    public List<GraphQLModel> getGraphqlModel() {
        return graphqlModel;
    }

    public void setGraphqlModel(List<GraphQLModel> graphqlModel) {
        this.graphqlModel = graphqlModel;
    }

    public List<CustomScalar> getCustomScalars() {
        return customScalars;
    }

    public void setCustomScalars(List<CustomScalar> customScalars) {
        this.customScalars = customScalars;
    }
}
