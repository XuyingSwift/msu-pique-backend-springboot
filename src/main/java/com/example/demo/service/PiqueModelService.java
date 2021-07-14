package com.example.demo.service;

import com.example.demo.config.FBInitialize;
import com.example.demo.model.PiqueModel;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PiqueModelService {
    @Autowired
    FBInitialize db;

    public static final String COLLECTION_NAME = "raws";

    public PiqueModel getParsedPiqueModel(String id) throws ExecutionException, InterruptedException, JsonProcessingException {
        DocumentReference documentReference = db.getFirebase().collection(COLLECTION_NAME).document(id);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot documentSnapshot = future.get();

        PiqueModel model = null;
        if (documentSnapshot.exists()) {
            System.out.println("Document data" + documentSnapshot.getData());
            model = documentSnapshot.toObject(PiqueModel.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.valueToTree(model.getTqi());
            ObjectNode node = Parser.getModel(root, mapper);
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            Map<String, Object> map = mapper.readValue(json, Map.class);
            PiqueModel piqueModel = new PiqueModel(map);
            return piqueModel;
        }else {
            System.out.println("Document not found");
            return null;
        }
    }

}

