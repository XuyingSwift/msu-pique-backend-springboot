package com.example.demo.model;

public class PiqueParsedModel {
    private String projectName;
    private String json;

    public PiqueParsedModel(String projectName, String json) {
        this.projectName = projectName;
        this.json = json;
    }

    public PiqueParsedModel() {}

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }



    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "PiqueParsedModel{" +
                "projectName='" + projectName + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
