package net.faithgen.sdk.comments;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.faithgen.sdk.R;
import net.faithgen.sdk.SDK;
import net.faithgen.sdk.http.API;
import net.faithgen.sdk.http.ErrorResponse;
import net.faithgen.sdk.http.Pagination;
import net.faithgen.sdk.http.Response;
import net.faithgen.sdk.http.types.ServerResponse;
import net.faithgen.sdk.models.Comment;
import net.faithgen.sdk.singletons.GSONSingleton;
import net.faithgen.sdk.utils.Constants;
import net.faithgen.sdk.utils.Dialogs;
import net.innoflash.iosview.swipelib.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;

public class CommentsUtil implements SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private CommentsSettings commentsSettings;
    private RelativeLayout signInLayout;
    private RelativeLayout commentLayout;
    private Button signIn;
    private FloatingActionButton commentFAB;
    private TextView noComments;
    private EditText commentField;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView commentsView;
    private Pagination pagination;
    private CommentsResponse commentsResponse;
    private CommentsAdapter adapter;
    private List<Comment> comments;
    private HashMap<String, String> params;
    private Response response;

    public List<Comment> getComments() {
        return comments;
    }

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
        noComments = view.findViewById(R.id.no_comments);

        swipeRefreshLayout.setPullPosition(Gravity.TOP);
        commentsView.setLayoutManager(new LinearLayoutManager(context));

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
        if(comments == null || comments.size() == 0){
            comments = commentsResponse.getComments();
            adapter = new CommentsAdapter(context, comments);
            commentsView.setAdapter(adapter);
            commentsView.smoothScrollToPosition(comments.size() - 1);
        }else{
            comments.addAll(0, commentsResponse.getComments());
            adapter.notifyDataSetChanged();
            commentsView.smoothScrollToPosition(commentsResponse.getComments().size() + 1);
        }

        initNoComments();
    }

    private void initNoComments() {
        if(comments == null || comments.size() == 0) noComments.setVisibility(View.VISIBLE);
        else noComments.setVisibility(View.GONE);
    }

    private void signInProfile() {
        Toast.makeText(context, commentsSettings.getFieldName(), Toast.LENGTH_SHORT).show();
    }

    private void sendComment() {
        if(commentField.getText().toString().isEmpty()) Dialogs.showOkDialog(context, Constants.BLANK_COMMENT, false);
        else{
            params = new HashMap<>();
            params.put(commentsSettings.getFieldName(), commentsSettings.getItemId());
            params.put(Constants.COMMENT, commentField.getText().toString());
            API.post(context, commentsSettings.getCategory() + "comment", params, false, new ServerResponse() {
                @Override
                public void onServerResponse(String serverResponse) {
                    response = GSONSingleton.getInstance().getGson().fromJson(serverResponse, Response.class);
                    if(response.isSuccess()){
                        processSuccessfulRequest(response);
                    }else Dialogs.showOkDialog(context, response.getMessage(), false);
                }

                @Override
                public void onResponse(String serverResponse) {
                    super.onResponse(serverResponse);
                }
            });
        }
    }

    private void processSuccessfulRequest(Response response) {
        commentField.setText("");
        Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
        comments.add(response.getComment());
        commentsView.smoothScrollToPosition(comments.size()-1);
        initNoComments();
    }

    @Override
    public void onRefresh() {
        if(pagination == null || pagination.getLinks().getNext() == null){
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, Constants.REACHED_END, Toast.LENGTH_SHORT).show();
        }else loadComments(pagination.getLinks().getNext());
    }
}
