package net.faithgen.sdk.menu;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public abstract class Menu {
    private List<MenuItem> menuItems;
    private MenuListener menuListener;
    private AppCompatActivity activity;
    private String title;

    /**
     * Opens the menu
     */
    public abstract void show();

    /**
     * Sets up the menu
     */
    public abstract void setUpMenu();

    /**
     * Distributes the params and activity to all menu providers
     * @param menuItems
     * @param activity
     */
    public Menu(List<MenuItem> menuItems, AppCompatActivity activity) {
        this.menuItems = menuItems;
        this.activity = activity;
    }

    /**
     * Gets the menu items in the children
     * @return
     */
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * Overrides the menu items provided by the initializer
     * @param menuItems
     */
    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    /**
     * Gets the click listener to effect actions
     * @return
     */
    public MenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * Avails the calling activity to context and all
     * @return
     */
    public AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * Sets the menu title for the iOSMenu
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the menu title and sets the event listeners
     * @param title
     * @param menuListener
     */
    public void setOnMenuItemListener(String title, MenuListener menuListener) {
        this.title = title;
        this.menuListener = menuListener;
    }
}
