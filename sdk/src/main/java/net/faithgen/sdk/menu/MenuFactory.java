package net.faithgen.sdk.menu;

import androidx.appcompat.app.AppCompatActivity;

import net.faithgen.sdk.SDK;
import net.faithgen.sdk.menu.choices.ContextualMenu;
import net.faithgen.sdk.menu.choices.iOSMenu;

import java.util.List;

public class MenuFactory {
    static List<MenuItem> menuItems;

    /**
     * Creates the menu for you ready for calling
     * @param activity
     * @param menuItems
     * @return Menu
     */
    public static Menu initializeMenu(AppCompatActivity activity, List<MenuItem> menuItems) {
        Menu menu = null;
        if (SDK.getMenuChoice().equals(MenuChoice.CONTEXTUAL_MENU))
            menu = new ContextualMenu(menuItems, activity);
        else menu = new iOSMenu(menuItems, activity);
        menu.setUpMenu();
        return menu;
    }
}
