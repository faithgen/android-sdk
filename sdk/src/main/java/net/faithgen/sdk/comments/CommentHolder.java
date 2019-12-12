package net.faithgen.sdk.comments;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.faithgen.sdk.views.CommentView;

public class CommentHolder extends RecyclerView.ViewHolder {
    private CommentView commentView;
    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        commentView = (CommentView) itemView;
    }

    public CommentView getCommentView() {
        return commentView;
    }
}
