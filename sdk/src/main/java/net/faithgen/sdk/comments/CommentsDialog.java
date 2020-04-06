package net.faithgen.sdk.comments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.faithgen.sdk.R;
import net.innoflash.iosview.DialogFullScreen;
import net.innoflash.iosview.DialogToolbar;

public class CommentsDialog extends DialogFullScreen {
    public static String TAG = "comments_dialogx";

    private View view;
    private CommentsSettings commentsSettings;
    private DialogToolbar dialogToolbar;
    private CommentsUtil commentsUtil;
    private Context context;

    public CommentsDialog(Context context, CommentsSettings commentsSettings) {
        this.commentsSettings = commentsSettings;
        this.context = context;
        this.commentsUtil = new CommentsUtil((Activity) this.context, this.commentsSettings);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_comments, container);
        commentsUtil.initViews(view);
        dialogToolbar = view.findViewById(R.id.dialog_toolbar);

        dialogToolbar.setDialogFragment(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialogToolbar.setTitle(commentsSettings.getTitle());
        if (commentsUtil.getComments() == null){
            commentsUtil.loadComments(commentsSettings.getCommentsRoute());
        }
        commentsUtil.connectPusher();
    }

    @Override
    public void onStop() {
        super.onStop();
        commentsUtil.getCommentsChannel().unbind(commentsUtil.getChannel(), null);
        commentsUtil.getPusher().disconnect();
    }
}
