package com.madhur.ngo_app.daos;


import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.madhur.ngo_app.models.Post;
import com.madhur.ngo_app.models.User;

import java.util.Objects;

public class PostDao {

    private static final String TAG = "PostDao";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference postCollections = db.collection("posts");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    boolean isLiked = false;
    Post postForLike;
    Task<DocumentSnapshot> documentSnapshotTask;


    public void addPost(String text) {
        String currentUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UserDao userDao = new UserDao();
        User user = new User();

        new Thread(() -> {
            Log.d(TAG, "addPost: CurrentThreadID : " + Thread.currentThread().getName());

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
        }).start();

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

    private Task<DocumentSnapshot> getPostById(String postID) {

        return postCollections.document(postID).get();
    }

    public void onUpdateLikes(String postId) {
        String currentUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Log.d(TAG, "onUpdateLikes: currentThreadOutside: " + Thread.currentThread().getName());

//        new Thread(() -> {
//            getPostById(postId).addOnSuccessListener(documentSnapshot -> {
//                postForLike = documentSnapshot.toObject(Post.class);
//                isLiked = postForLike.getLikedBy().contains(currentUid);
//
//                if (isLiked) postForLike.likedBy.remove(currentUid);
//                else postForLike.likedBy.add(currentUid);
//
//                new Thread(() -> postCollections.document(postId).set(postForLike)).start();
//            }).addOnFailureListener(e -> {
//            });
//        }).start();

        getPostById(postId).addOnSuccessListener(documentSnapshot -> {
            postForLike = documentSnapshot.toObject(Post.class);
            isLiked = postForLike.getLikedBy().contains(currentUid);

            if (isLiked) postForLike.likedBy.remove(currentUid);
            else postForLike.likedBy.add(currentUid);

            postCollections.document(postId).set(postForLike);
        }).addOnFailureListener(e -> {
        });

    }

}
