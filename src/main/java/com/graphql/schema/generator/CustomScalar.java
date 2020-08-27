package com.graphql.schema.generator;

public class CustomScalar {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomScalar(String name) {
        this.name = name;
    }
}
