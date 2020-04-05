package net.faithgen.sdk.comments;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;

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
    private Activity context;
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
    private HashMap<String, String> headers;
    private Response response;
    private FaithGenAPI faithGenAPI;
    private HttpAuthorizer httpAuthorizer;
    private PusherOptions pusherOptions;
    public Pusher pusher;
    public Channel commentsChannel;

    public List<Comment> getComments() {
        return comments;
    }

    public CommentsUtil(Activity context, CommentsSettings commentsSettings) {
        this.context = context;
        this.commentsSettings = commentsSettings;

        headers = new HashMap<>();
        headers.put("X-App-ID", "myId");

        this.httpAuthorizer = new HttpAuthorizer(getServer() + "/laravel-websockets/auth");
        this.httpAuthorizer.setHeaders(headers);

        this.pusherOptions = new PusherOptions()
                .setHost(getHost())
                .setWsPort(6001)
                .setWssPort(6001)
                .setAuthorizer(httpAuthorizer)
                .setEncrypted(SDK.getApiBase().contains("https://"));

        this.pusher = new Pusher("myKey", pusherOptions);
    }

    /**
     * Connects to the websocket to wire instant messaging.
     */
    void connectPusher() {
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {

            }

            @Override
            public void onError(String message, String code, Exception e) {

            }
        }, ConnectionState.ALL);

        commentsChannel = pusher.subscribePrivate("private-comments-sermons--5-3e4c7f160-b5191986fb06-be8fe23ad2");

        //bind comment created
        commentsChannel.bind("comment.created", new PrivateChannelEventListener() {
            @Override
            public void onAuthenticationFailure(String message, Exception e) {

            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {

            }

            @Override
            public void onEvent(PusherEvent event) {
                response = GSONSingleton.Companion.getInstance().getGson().fromJson(event.getData(), Response.class);
                if (SDK.getUser() == null || !SDK.getUser().getId().equals(response.getComment().getCreator().getId()))
                    processSuccessfulRequest(response.getComment());
            }
        });
    }

    /**
     * Initialized the views used for commenting
     *
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
     *
     * @param url The source of the comments
     */
    public void loadComments(String url) {
        faithGenAPI = new FaithGenAPI(context)
                .setParams(commentsSettings.getParams())
                .setMethod(Request.Method.GET)
                .setProcess(Constants.FETCHING_COMMENTS)
                .setServerResponse(new ServerResponse() {
                    @Override
                    public void onServerResponse(String serverResponse) {
                        populateComments(serverResponse);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(ErrorResponse errorResponse) {
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
     *
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
                    .setServerResponse(new ServerResponse() {
                        @Override
                        public void onServerResponse(String serverResponse) {
                            response = GSONSingleton.Companion.getInstance().getGson().fromJson(serverResponse, Response.class);
                            if (response.getSuccess()) {
                                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                                processSuccessfulRequest(response.getComment());
                            } else Dialogs.showOkDialog(context, response.getMessage(), false);
                        }

                        @Override
                        public void onError(ErrorResponse errorResponse) {
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
     * @param comment
     */
    private void processSuccessfulRequest(Comment comment) {
        commentField.setText("");
        comments.add(comment);

        if (comments.size() == 1) {
            adapter = new CommentsAdapter(context, comments);
            commentsView.setAdapter(adapter);
        } else {
            context.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                commentsView.smoothScrollToPosition(comments.size() - 1);
            });
        }
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

    /**
     * Get the server host.
     *
     * @return
     */
    private String getHost() {
        return getServer().replace(":8001", "")
                .replace("http://", "")
                .replace("https://", "");
    }

    /**
     * Gets the server running the API.
     *
     * @return
     */
    private String getServer() {
        return SDK.getApiBase().replace("/api/", "");
    }

    /**
     * Get the channel to subscribe to.
     *
     * @return
     */
    public String getChannel() {
        return "private-comments-" + commentsSettings.getCategory().replace("/", "-" + commentsSettings.getItemId());
    }
}
