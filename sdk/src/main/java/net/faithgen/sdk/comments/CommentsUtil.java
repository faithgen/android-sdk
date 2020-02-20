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

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.faithgen.sdk.R;
import net.faithgen.sdk.SDK;
import net.faithgen.sdk.http.ErrorResponse;
import net.faithgen.sdk.http.FaithGenAPI;
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

/**
 * Handles commenting of items
 */
public final class CommentsUtil implements SwipeRefreshLayout.OnRefreshListener {
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
    private FaithGenAPI faithGenAPI;

    public List<Comment> getComments() {
        return comments;
    }

    public CommentsUtil(Context context, CommentsSettings commentsSettings) {
        this.context = context;
        this.commentsSettings = commentsSettings;
    }

    /**
     * Initialized the views used for commenting
     * @param view
     */
    public void initViews(View view) {
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

    /**
     * This decides whether to display the comment field or
     * let the user know they still have to sign in
     */
    private void initFooterLayouts() {
        if (SDK.getUser() != null) signInLayout.setVisibility(View.GONE);
        else commentLayout.setVisibility(View.GONE);
    }

    /**
     * This sets the event listeners for this section
     */
    private void initViewsEvents() {
        commentFAB.setOnClickListener(v -> sendComment());
        signIn.setOnClickListener(v -> signInProfile());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * This loads the item comments from the server
     * @param url The source of the comments
     */
    public void loadComments(String url) {
        faithGenAPI = new FaithGenAPI(context)
                .setParams(commentsSettings.getParams())
                .setMethod(Request.Method.GET)
                .setServerResponse(new ServerResponse() {
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
                        noComments.setText(errorResponse.getMessage());
                    }
                });
        faithGenAPI.request(url);
    }

    /**
     * This coverts the comments into native objects
     * and then render them on the selected view
     * @param serverResponse
     */
    private void populateComments(String serverResponse) {
        pagination = GSONSingleton.Companion.getInstance().getGson().fromJson(serverResponse, Pagination.class);
        commentsResponse = GSONSingleton.Companion.getInstance().getGson().fromJson(serverResponse, CommentsResponse.class);
        if (comments == null || comments.size() == 0) {
            comments = commentsResponse.getComments();
            adapter = new CommentsAdapter(context, comments);
            commentsView.setAdapter(adapter);
            try {
                commentsView.smoothScrollToPosition(comments.size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            comments.addAll(0, commentsResponse.getComments());
            adapter.notifyDataSetChanged();
            try {
                commentsView.smoothScrollToPosition(commentsResponse.getComments().size() + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initNoComments();
    }

    /**
     * Handles what to display of no comments are found for a certain item
     */
    private void initNoComments() {
        if (comments == null || comments.size() == 0) {
            noComments.setVisibility(View.VISIBLE);
            if (noComments.getText().toString().contains("could"))
                noComments.setText("No comments found for this item");
        } else noComments.setVisibility(View.GONE);
    }

    /**
     * This takes the use to the sign in page
     */
    private void signInProfile() {
        if (SDK.getUserAuthListener() != null) SDK.getUserAuthListener().onSignInTapped();
    }

    /**
     * This sends the user`s comment to the given item
     */
    private void sendComment() {
        if (commentField.getText().toString().isEmpty())
            Dialogs.showOkDialog(context, Constants.BLANK_COMMENT, false);
        else {
            params = new HashMap<>();
            params.put(commentsSettings.getFieldName(), commentsSettings.getItemId());
            params.put(Constants.COMMENT, commentField.getText().toString());

            faithGenAPI = new FaithGenAPI(context)
                    .setParams(params)
                    .setMethod(Request.Method.POST)
                    .setProcess(Constants.SENDING_COMMENT)
                    .setProcess(Constants.FETCHING_COMMENTS)
                    .setServerResponse(new ServerResponse() {
                        @Override
                        public void onServerResponse(String serverResponse) {
                            response = GSONSingleton.Companion.getInstance().getGson().fromJson(serverResponse, Response.class);
                            if (response.getSuccess()) {
                                processSuccessfulRequest(response);
                            } else Dialogs.showOkDialog(context, response.getMessage(), false);
                        }

                        @Override
                        public void onError(ErrorResponse errorResponse) {
                            //super.onError(errorResponse);
                            Dialogs.showOkDialog(context, errorResponse.getMessage(), false);
                        }
                    });
            faithGenAPI.request(commentsSettings.getCategory() + "comment");
        }
    }

    /**
     * This processes the response from the server when a
     * comment is sent successfully
     *
     * @param response
     */
    private void processSuccessfulRequest(Response response) {
        commentField.setText("");
        Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
        comments.add(response.getComment());
        commentsView.smoothScrollToPosition(comments.size() - 1);
        initNoComments();
    }

    /**
     * This handles the comments swipe refresh
     */
    @Override
    public void onRefresh() {
        if (pagination == null || pagination.getLinks().getNext() == null) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, Constants.REACHED_END, Toast.LENGTH_SHORT).show();
        } else loadComments(pagination.getLinks().getNext());
    }
}
