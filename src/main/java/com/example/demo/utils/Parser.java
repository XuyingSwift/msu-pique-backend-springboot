package com.example.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;

public class Parser {

    public static ObjectNode getModel(JsonNode root, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();

        //Float value = root.get("factors").get("tqi").get("Binary Security Quality").get("value").floatValue();
        Float value = root.get("tqi").get("Binary Security Quality").get("value").floatValue();

        objectNode.put("name", "Binary Security Quality");
        objectNode.put("value", value);
        //Iterator<JsonNode> kids = root.get("factors").get("tqi").get("Binary Security Quality").get("children").elements();
        Iterator<JsonNode> kids = root.get("tqi").get("Binary Security Quality").get("children").elements();
        //Iterator<JsonNode> weights = root.get("factors").get("tqi").get("Binary Security Quality").get("weights").elements();
        Iterator<JsonNode> weights = root.get("tqi").get("Binary Security Quality").get("weights").elements();
        ArrayNode qa = getQuality_aspects(kids, weights, mapper);
        objectNode.put("children", qa);
        return objectNode;

    }

    private static ObjectNode getObjectNode(JsonNode next, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String name = next.get("name").asText();
        Float value = next.get("value").floatValue();
        objectNode.put("name", name);
        objectNode.put("value", value);
        return objectNode;
    }

    private static ArrayNode getQuality_aspects(Iterator<JsonNode> qualityAspects, Iterator<JsonNode> weights, ObjectMapper mapper) {
        ArrayNode list = mapper.createArrayNode();

        while (qualityAspects.hasNext() && weights.hasNext()) {
            JsonNode next = qualityAspects.next();
            JsonNode weight = weights.next();
            Iterator<JsonNode> category = next.get("children").elements();
            ObjectNode node = getObjectNode(next, mapper);
            node.put("children", getProductFactors(category, mapper));
            list.add(node);
        }
        return list;
    }

    // product-factors
    private static ArrayNode getProductFactors( Iterator<JsonNode> category, ObjectMapper mapper) {
        ArrayNode list = mapper.createArrayNode();
        while (category.hasNext()) {
            JsonNode next = category.next();
            Iterator<JsonNode> measures = next.get("children").elements();
            ObjectNode objectNode = getObjectNode(next, mapper);
            objectNode.put("children", getMeasures(measures, mapper));
            list.add(objectNode);
        }
        return list;

    }

    // measures
    private static ArrayNode getMeasures(Iterator<JsonNode> measures, ObjectMapper mapper) {
        ArrayNode list = mapper.createArrayNode();
        while (measures.hasNext()) {
            JsonNode next = measures.next();
            Iterator<JsonNode> instance = next.get("children").elements();
            ObjectNode objectNode = getObjectNode(next, mapper);
            objectNode.put("children", getDiagnostics(instance, mapper));
            list.add(objectNode);
        }
        return list;
    }

    // diagnostics

    private static ArrayNode getDiagnostics(Iterator<JsonNode> measures, ObjectMapper mapper) {
        ArrayNode list = mapper.createArrayNode();
        while (measures.hasNext()) {
            JsonNode next = measures.next();
            Iterator<JsonNode> diagnostics = next.get("children").elements();
            ObjectNode objectNode = getObjectNode(next, mapper);
            list.add(objectNode);
        }
        return list;
    }
}
