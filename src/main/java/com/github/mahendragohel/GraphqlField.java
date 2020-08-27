package com.github.mahendragohel;

public class GraphqlField {
    private String fieldDescription;
    private String fieldName;
    private String fieldType;

    public GraphqlField(String fieldDescription, String fieldName, String fieldType) {
        this.fieldDescription = fieldDescription;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
