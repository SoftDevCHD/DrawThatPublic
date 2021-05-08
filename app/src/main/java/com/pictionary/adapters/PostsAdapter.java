package com.pictionary.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.pictionary.Post;
import com.pictionary.R;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPicture;
        private TextView tvUser;
        private TextView tvDescription;
        private final View vdivider;

        public ViewHolder(View itemView) {
            super(itemView);

            vdivider = itemView.findViewById(R.id.vDivider);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Post post) {
            tvUser.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            ParseFile pic = post.getImage();
            if (pic != null) {
                Glide.with(context).load(pic.getUrl()).into(ivPicture);
            }
            else {
                Glide.with(context).load(R.drawable.ic_launcher_background).into(ivPicture);
            }
        }
    }
}
