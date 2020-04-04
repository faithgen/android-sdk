package net.faithgen.sdk.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import net.faithgen.sdk.R;
import net.faithgen.sdk.SDK;
import net.faithgen.sdk.models.Comment;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentView extends LinearLayout {
    private TextView userNameView;
    private TextView userCommentView;
    private TextView commentTimeView;
    private CircleImageView circularImageView;
    private CircleImageView meImageView;
    private LinearLayout commentlayout;
    private String userName;
    private String userComment;
    private String time;
    private Comment comment;

    public CommentView(Context context) {
        super(context);
        init();
    }

    public CommentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommentView);
        readAttributes(typedArray);
        typedArray.recycle();
    }

    private void readAttributes(TypedArray typedArray) {
        setUserName(typedArray.getString(R.styleable.CommentView_user_name));
        setUserComment(typedArray.getString(R.styleable.CommentView_user_comment));
        setTime(typedArray.getString(R.styleable.CommentView_comment_time));
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_comment_item, this);
        userNameView = findViewById(R.id.user_name);
        userCommentView = findViewById(R.id.user_comment);
        commentTimeView = findViewById(R.id.comment_time);
        circularImageView = findViewById(R.id.user_image);
        meImageView = findViewById(R.id.me_image);
        commentlayout = findViewById(R.id.comment_layout);
    }

    public TextView getUserNameView() {
        return userNameView;
    }

    public TextView getUserCommentView() {
        return userCommentView;
    }

    public TextView getCommentTimeView() {
        return commentTimeView;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserComment() {
        return userComment;
    }

    public String getTime() {
        return time;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        getUserNameView().setText(userName);
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
        getUserCommentView().setText(userComment);
    }

    public void setTime(String time) {
        this.time = time;
        getCommentTimeView().setText(time);
    }

    public CircleImageView getCircularImageView() {
        return circularImageView;
    }

    public void setCircularImageView(CircleImageView circularImageView) {
        this.circularImageView = circularImageView;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        if (comment.getCreator().is_admin())
            commentlayout.setBackground(getResources().getDrawable(R.drawable.chat_admin_background));

        if (SDK.getUser() == null || !SDK.getUser().getId().equals(comment.getCreator().getId()))
            setUserName(comment.getCreator().getName());
        else {
            setUserName(comment.getCreator().getName() + " (you)");
            getUserNameView().setGravity(Gravity.END);
            commentlayout.setBackground(getResources().getDrawable(R.drawable.me_chat_background));

            getCircularImageView().setVisibility(INVISIBLE);
            meImageView.setVisibility(VISIBLE);
        }

        setUserComment(comment.getComment());
        setTime(comment.getDate().getApprox());

        Picasso.get()
                .load(comment.getCreator().getAvatar().get_50())
                .error(R.drawable.commenter)
                .placeholder(R.drawable.commenter)
                .into(getCircularImageView());

        Picasso.get()
                .load(comment.getCreator().getAvatar().get_50())
                .error(R.drawable.commenter)
                .placeholder(R.drawable.commenter)
                .into(meImageView);
    }
}
