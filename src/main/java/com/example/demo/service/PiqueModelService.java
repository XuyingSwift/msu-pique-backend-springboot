package com.example.demo.service;

import com.example.demo.config.FBInitialize;
import com.example.demo.model.PiqueModel;
import com.example.demo.model.PiqueParsedModel;
import com.example.demo.utils.Parser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class PiqueModelService {
    @Autowired
    FBInitialize db;

    public static final String COLLECTION_NAME = "rawPiqueJsons";

    public PiqueParsedModel getParsedPiqueFile(String name) throws ExecutionException, InterruptedException, IOException {
        DocumentReference docRef = db.getFirebase().collection("rawPiqueJsons").document(name);
// asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
// block on response
        DocumentSnapshot document = future.get();
        PiqueModel model = null;
        if (document.exists()) {
            // convert document to POJO
            model = document.toObject(PiqueModel.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.valueToTree(model);
            ObjectNode node = Parser.getModel(root, mapper);
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

            PiqueParsedModel parsedModel = new PiqueParsedModel(name, json);
            return parsedModel;
        } else {
            return null;
        }
    }

}
