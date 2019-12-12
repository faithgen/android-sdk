package net.faithgen.sdk.comments;

import net.faithgen.sdk.models.Comment;

import java.util.Collections;
import java.util.List;

public class CommentsResponse {
    private List<Comment> comments;

    public List<Comment> getComments() {
        Collections.reverse(comments);
        return comments;
    }
}
