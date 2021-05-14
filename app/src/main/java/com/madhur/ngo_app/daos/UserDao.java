package com.madhur.ngo_app.daos;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.madhur.ngo_app.models.User;

import javax.annotation.Nullable;

public class UserDao {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = db.collection("users");
    Task<DocumentSnapshot> documentSnapshotTask;


    public void addUser(@Nullable User user) {
        if (user != null) {
            usersCollection.document(user.getUid()).set(user);
        }
    }

    public Task<DocumentSnapshot> getUserById(String uId) {

//        new Thread(() -> documentSnapshotTask =  usersCollection.document(uId).get());
//        return documentSnapshotTask;
//
        return usersCollection
                .document(uId)
                .get();
    }
}
