package com.example.demo.controller;

import com.example.demo.config.FBInitialize;
import com.example.demo.model.PiqueParsedModel;
import com.example.demo.service.PiqueModelService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class PiqueParsedModelController {

    @Autowired
    FBInitialize db;

    @Autowired
    PiqueModelService service;

    @PostMapping("/saveEmployee")
    public String savePiqueOutput(@RequestParam String name) throws ExecutionException, InterruptedException, IOException {
        /*CollectionReference piqueOutput = db.getFirebase().collection("rawPiqueJsons");
        piqueOutput.document(output.getName()).set(output);
        return output.getName();*/
        /*Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("projects").document(output.getName()).set(output);
        return collectionsApiFuture.get().getUpdateTime().toString();
         */
        PiqueParsedModel parsedModel = service.getParsedPiqueFile(name);
        System.out.println(parsedModel.getProjectName());
        ApiFuture<WriteResult> future = db.getFirebase().collection("ParsedProjects").document(name).set(parsedModel);
        return (parsedModel.getProjectName() + future.get().getUpdateTime().toString());
    }

    @GetMapping("/getPiqueParsedModel")
    public PiqueParsedModel getPiqueParsedModel(@RequestParam String name) throws ExecutionException, InterruptedException {
        DocumentReference piqueRef = db.getFirebase().collection("ParsedProjects").document(name);
        ApiFuture<DocumentSnapshot> future = piqueRef.get();
        DocumentSnapshot documentSnapshot = future.get();
        if (documentSnapshot.exists()) {
            return documentSnapshot.toObject(PiqueParsedModel.class);
        } else {
            return null;
        }
    }
}
