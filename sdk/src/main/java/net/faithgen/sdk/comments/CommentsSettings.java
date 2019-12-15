package net.faithgen.sdk.comments;

import net.faithgen.sdk.enums.CommentsDisplay;
import net.faithgen.sdk.utils.Constants;

import java.util.HashMap;

import nouri.in.goodprefslib.GoodPrefs;

public class CommentsSettings {
    private static final String LIMIT = "limit";
    private String itemId;
    private String title;
    private String category;
    private int limit;
    private HashMap<String, String> params;
    private CommentsDisplay commentsDisplay;

    public CommentsDisplay getCommentsDisplay() {
        return commentsDisplay;
    }

    public HashMap<String, String> getParams() {
        params = new HashMap<>();
        params.put(LIMIT, getLimit() + "");
        return params;
    }

    public int getLimit() {
        return limit;
    }

    public String getFieldName() {
        return category.substring(0, category.indexOf("s/")) + "_id";
    }

    public String getCommentsRoute() {
        return getCategory() + "comments/" + getItemId();
    }

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category.contains("/") ? category : category + "/";
    }

    private CommentsSettings() {
    }

    public static class Builder {
        private String itemId;
        private String title = Constants.COMMENTS;
        private String category;
        private int limit = GoodPrefs.getInstance().getInt(Constants.LIMIT, 15);
        private CommentsSettings commentsSettings;
        private CommentsDisplay commentsDisplay = getCommentsDisplay();

        private CommentsDisplay getCommentsDisplay(){
            if(GoodPrefs.getInstance().getObject(Constants.COMMENTS_DISPLAY, CommentsDisplay.class) == null) return CommentsDisplay.DIALOG;
            else return GoodPrefs.getInstance().getObject(Constants.COMMENTS_DISPLAY, CommentsDisplay.class);
        }

        public Builder setCommentsDisplay(CommentsDisplay commentsDisplay) {
            this.commentsDisplay = commentsDisplay;
            return this;
        }

        public Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder setItemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public CommentsSettings build() {
            commentsSettings = new CommentsSettings();
            commentsSettings.itemId = itemId;
            commentsSettings.title = title;
            commentsSettings.category = category;
            commentsSettings.limit = limit;
            commentsSettings.commentsDisplay = commentsDisplay;
            return commentsSettings;
        }
    }
}
