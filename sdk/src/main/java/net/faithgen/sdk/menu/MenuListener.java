package net.faithgen.sdk.menu;

public interface MenuListener {
    /**
     * Initiates button item clicks
     * @param menuItem
     * @param position
     */
    void itemClick(MenuItem menuItem, int position);
}
