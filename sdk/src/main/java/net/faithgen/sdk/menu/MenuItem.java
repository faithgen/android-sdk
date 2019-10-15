package net.faithgen.sdk.menu;

import net.faithgen.sdk.R;

public class MenuItem {
    private int icon;
    private String text;
    private boolean cancelled;

    public MenuItem(String text) {
        this.text = text;
        this.icon = R.drawable.ic_more_vert_black_18dp;
    }

    public MenuItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public MenuItem(int icon, String text, boolean cancelled) {
        this.icon = icon;
        this.text = text;
        this.cancelled = cancelled;
    }

    public int getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
