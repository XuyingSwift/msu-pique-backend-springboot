package com.example.demo.controller;

import com.example.demo.config.FBInitialize;
import com.example.demo.model.PiqueModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class PiqueModelController {
    @Autowired
    FBInitialize db;

    @GetMapping("/getPiqueOutputs")
    public List<PiqueModel> getAllPiqueOutputs () throws ExecutionException, InterruptedException {
        List <PiqueModel>  outputList = new ArrayList<>();
        CollectionReference output = db.getFirebase().collection("rawPiqueJsons");
        ApiFuture<QuerySnapshot> querySnapshot = output.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            PiqueModel out = doc.toObject(PiqueModel.class);
            outputList.add(out);
        }
        return outputList;
    }

    @GetMapping("/getPiqueOutput")
    public PiqueModel getPiqueOutput(@RequestParam String name) throws ExecutionException, InterruptedException {
        DocumentReference piqueRef = db.getFirebase().collection("rawPiqueJsons").document(name);
        ApiFuture<DocumentSnapshot> future = piqueRef.get();
        DocumentSnapshot documentSnapshot = future.get();
        if (documentSnapshot.exists()) {
            return documentSnapshot.toObject(PiqueModel.class);
        } else {
            return null;
        }
    }
}
