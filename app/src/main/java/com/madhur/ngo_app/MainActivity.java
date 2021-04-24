package com.madhur.ngo_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.madhur.ngo_app.daos.PostDao;
import com.madhur.ngo_app.models.Post;

public class MainActivity extends AppCompatActivity implements IPostAdapter {

    PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(this, CreatePostActivity.class));
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        CollectionReference postsCollection = FirebaseFirestore.getInstance().collection("posts");
        Query query = postsCollection.orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Post> recyclerOptions = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        postAdapter = new PostAdapter(recyclerOptions, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        postAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }

    @Override
    public void onLikeClicked(String postId) {
        new PostDao().onUpdateLikes(postId);
    }
}