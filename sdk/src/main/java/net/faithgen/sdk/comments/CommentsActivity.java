package net.faithgen.sdk.comments;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import net.faithgen.sdk.FaithGenActivity;
import net.faithgen.sdk.R;
import net.faithgen.sdk.singletons.GSONSingleton;
import net.faithgen.sdk.utils.Constants;

public class CommentsActivity extends FaithGenActivity {

    private CommentsUtil commentsUtil;
    private CommentsSettings commentsSettings;

    @Override
    public String getPageTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_comments);

        commentsSettings = GSONSingleton.Companion.getInstance().getGson().fromJson(getIntent().getStringExtra(Constants.SETTINGS), CommentsSettings.class);
        commentsUtil = new CommentsUtil(this, commentsSettings);

        commentsUtil.initViews(getView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getToolbar().setPageTitle(commentsSettings.getTitle());
        if (commentsUtil.getComments() == null) {
            commentsUtil.loadComments(commentsSettings.getCommentsRoute());
        }
        commentsUtil.connectPusher();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            commentsUtil.getCommentsChannel().unbind(commentsUtil.getChannel(), null);
            commentsUtil.getPusher().disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
