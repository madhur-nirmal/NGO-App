package com.madhur.ngo_app.daos;


import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.madhur.ngo_app.models.Post;
import com.madhur.ngo_app.models.User;

import java.util.Map;
import java.util.Objects;

public class PostDao {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference postCollections = db.collection("posts");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public void addPost(String text) {
        String currentUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UserDao userDao = new UserDao();
        User user = new User();

        userDao
                .getUserById(currentUid)
                .addOnSuccessListener(documentSnapshot -> {
                    user.setName(documentSnapshot.getString("name"));
                    user.setImageUrl(documentSnapshot.getString("imageUrl"));
                    user.setUid(documentSnapshot.getId());
                    makePost(text, user);
                    Log.d("Name", "addPost: " + documentSnapshot.getString("name"));
                })

                .addOnFailureListener(e -> {
                    Log.d("Failure", "addPost: Failed");
                });
        
    }

    private void makePost(String text, User user) {
        Log.d("Name", "message: " + user.getName());
        Long currentTime = System.currentTimeMillis();
        Post post = new Post();
        post.setCreatedAt(currentTime);
        post.setCreatedBy(user);
        post.setText(text);
        postCollections.document().set(post);
    }
}
