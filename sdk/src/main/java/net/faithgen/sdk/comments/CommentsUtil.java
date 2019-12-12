package net.faithgen.sdk.comments;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.faithgen.sdk.R;
import net.faithgen.sdk.SDK;
import net.faithgen.sdk.http.API;
import net.faithgen.sdk.http.ErrorResponse;
import net.faithgen.sdk.http.Pagination;
import net.faithgen.sdk.http.types.ServerResponse;
import net.faithgen.sdk.singletons.GSONSingleton;
import net.faithgen.sdk.utils.Constants;
import net.faithgen.sdk.utils.Dialogs;
import net.innoflash.iosview.swipelib.SwipeRefreshLayout;

public class CommentsUtil implements SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private CommentsSettings commentsSettings;
    private RelativeLayout signInLayout;
    private RelativeLayout commentLayout;
    private Button signIn;
    private FloatingActionButton commentFAB;
    private EditText commentField;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView commentsView;
    private Pagination pagination;
    private CommentsResponse commentsResponse;

    public CommentsUtil(Context context, CommentsSettings commentsSettings) {
        this.context = context;
        this.commentsSettings = commentsSettings;
    }

    public void initViews(View view){
        signInLayout = view.findViewById(R.id.sign_in_layout);
        commentLayout = view.findViewById(R.id.comment_layout);
        signIn = view.findViewById(R.id.sign_in);
        commentFAB = view.findViewById(R.id.send_comment);
        commentField = view.findViewById(R.id.comment_field);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        commentsView = view.findViewById(R.id.commentsView);

        swipeRefreshLayout.setPullPosition(Gravity.TOP);

        initViewsEvents();
        initFooterLayouts();
    }

    private void initFooterLayouts() {
        if(SDK.getUser() != null) signInLayout.setVisibility(View.GONE);
        else commentLayout.setVisibility(View.GONE);
    }

    private void initViewsEvents() {
        commentFAB.setOnClickListener(v -> sendComment());
        signIn.setOnClickListener(v -> signInProfile());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void loadComments(String url){
        API.get(context, url, commentsSettings.getParams(), false, new ServerResponse() {
            @Override
            public void onServerResponse(String serverResponse) {
                populateComments(serverResponse);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                //super.onError(errorResponse);
                swipeRefreshLayout.setRefreshing(false);
                Dialogs.showOkDialog(context, errorResponse.getMessage(), false);
            }
        });
    }

    private void populateComments(String serverResponse) {
        pagination = GSONSingleton.getInstance().getGson().fromJson(serverResponse, Pagination.class);
        commentsResponse = GSONSingleton.getInstance().getGson().fromJson(serverResponse, CommentsResponse.class);
    }

    private void signInProfile() {

    }

    private void sendComment() {

    }

    @Override
    public void onRefresh() {
        if(pagination == null || pagination.getLinks().getNext() == null){
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, Constants.REACHED_END, Toast.LENGTH_SHORT).show();
        }
    }
}
