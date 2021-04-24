package com.madhur.ngo_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.madhur.ngo_app.models.Post;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder> {

    IPostAdapter listener;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options, IPostAdapter listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
        holder.postText.setText(model.getText());
        holder.userName.setText(model.getCreatedBy().getName());
        Glide
                .with(holder.userImage.getContext())
                .load(model
                        .getCreatedBy()
                        .getImageUrl()
                )
                .circleCrop()
                .into(holder.userImage);
        holder.likedBy.setText(String.valueOf(model.getLikedBy().size()));
        holder.createdAt.setText(Util.getTimeAgo(model.getCreatedAt()));
        String auth = FirebaseAuth.getInstance().getUid();
        boolean isLiked = model.likedBy.contains(auth);
        if (isLiked) {
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(holder.likeBtn.getContext(), R.drawable.ic_liked));
        } else {
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(holder.likeBtn.getContext(), R.drawable.ic_unliked));
        }

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostViewHolder viewHolder = new PostViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_post, parent, false)
        );
        viewHolder.likeBtn.setOnClickListener(v -> {
            listener.onLikeClicked(getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getId());
        });
        return viewHolder;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView postText = itemView.findViewById(R.id.postText);
        TextView createdAt = itemView.findViewById(R.id.createdAt);
        TextView userName = itemView.findViewById(R.id.userName);
        ImageView userImage = itemView.findViewById(R.id.userImage);
        ImageView likeBtn = itemView.findViewById(R.id.likeBtn);
        TextView likedBy = itemView.findViewById(R.id.likedBy);



        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

        }

    }

}
interface IPostAdapter {
    void onLikeClicked(String postId);
}
