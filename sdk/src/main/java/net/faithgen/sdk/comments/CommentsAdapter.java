package net.faithgen.sdk.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.faithgen.sdk.R;
import net.faithgen.sdk.models.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentHolder> {
    private Context context;
    private List<Comment> comments;
    private LayoutInflater inflater;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.inflater = LayoutInflater.from(this.context);
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(inflater.inflate(R.layout.list_item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.getCommentView().setComment(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
