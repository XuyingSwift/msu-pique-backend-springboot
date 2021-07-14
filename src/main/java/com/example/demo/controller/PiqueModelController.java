package com.example.demo.controller;

import com.example.demo.config.FBInitialize;
import com.example.demo.model.PiqueModel;
import com.example.demo.service.PiqueModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class PiqueModelController {
    @Autowired
    FBInitialize db;

    @Autowired
    PiqueModelService service;

    @GetMapping("/getPiqueOutputs")
    public List<PiqueModel> getAllPiqueOutputs () throws ExecutionException, InterruptedException {
        List <PiqueModel>  outputList = new ArrayList<>();
        CollectionReference output = db.getFirebase().collection("raws");
        ApiFuture<QuerySnapshot> querySnapshot = output.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            PiqueModel out = doc.toObject(PiqueModel.class);
            outputList.add(out);
        }
        return outputList;
    }

    @GetMapping("/getPiqueOutput")
    public PiqueModel getPiqueOutput(@RequestParam String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.getFirebase().collection("raws").document(id);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        PiqueModel piqueModel = null;
        if (document.exists()) {

            // convert document to POJO
            piqueModel = document.toObject(PiqueModel.class);
            System.out.println("Document exists");
            return piqueModel;

        } else {
            System.out.println("no such file");
            return null;
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/postPiqueParsedModel")
    public String savePiqueParseModel(@RequestParam String id) throws ExecutionException, InterruptedException, JsonProcessingException {
        PiqueModel model = service.getParsedPiqueModel(id);
        ApiFuture<DocumentReference> future = db.getFirebase().collection("ParsedProjects").add(model);
        return future.get().getId();
    }
}
