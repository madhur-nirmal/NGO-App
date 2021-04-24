package com.madhur.ngo_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.madhur.ngo_app.daos.PostDao;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        EditText postInputText = findViewById(R.id.postInputBox);
        Button postBtn = findViewById(R.id.postBtn);
        PostDao postDao = new PostDao();
        postBtn.setOnClickListener(v -> {
            String input = postInputText.getText().toString().trim();
            if (!input.isEmpty()) {
                postDao.addPost(input);
            }
            finish();
        });
    }
}