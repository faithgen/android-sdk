package net.faithgen.sdk.menu.choices;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import net.faithgen.sdk.R;
import net.faithgen.sdk.menu.Menu;
import net.faithgen.sdk.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ContextualMenu extends Menu {

    private ContextMenuDialogFragment contextMenuDialogFragment;
    private MenuParams menuParams;
    private List<MenuObject> menuObjects;
    private MenuObject closeMenu;

    public ContextualMenu(List<MenuItem> menuItems, AppCompatActivity activity) {
        super(menuItems, activity);
    }


    @Override
    public void show() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            contextMenuDialogFragment.show(getActivity().getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
        }
    }

    @Override
    public void setUpMenu() {
        menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getActivity().getResources().getDimension(R.dimen.toolbar_height));

        menuObjects = new ArrayList<>();

        closeMenu = new MenuObject();
        closeMenu.setResourceValue(R.drawable.close);

        menuObjects.add(closeMenu);
        for (MenuItem menuItem :
                getMenuItems()) {
            MenuObject menuObject = new MenuObject(menuItem.getText());
            menuObject.setResourceValue(menuItem.getIcon());
            menuObjects.add(menuObject);
        }
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(false);
        contextMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        contextMenuDialogFragment.setMenuItemClickListener((view, integer) -> {
            switch (integer) {
                case 0:
                    contextMenuDialogFragment.dismiss();
                    break;
                default:
                    if (getMenuListener() != null)
                        getMenuListener().itemClick(null, integer - 1);
                    break;
            }
            return null;
        });
    }
}