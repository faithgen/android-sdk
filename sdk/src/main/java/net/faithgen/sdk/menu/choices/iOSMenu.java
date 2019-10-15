package net.faithgen.sdk.menu.choices;

import androidx.appcompat.app.AppCompatActivity;

import com.ligl.android.widget.iosdialog.IOSSheetDialog;

import net.faithgen.sdk.menu.Menu;
import net.faithgen.sdk.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class iOSMenu extends Menu {

    private List<IOSSheetDialog.SheetItem> sheetItems;
    private IOSSheetDialog iosSheetDialog;

    public iOSMenu(List<MenuItem> menuItems, AppCompatActivity activity) {
        super(menuItems, activity);
    }

    @Override
    public void show() {
        iosSheetDialog.show();
    }

    @Override
    public void setUpMenu() {
        sheetItems = new ArrayList<>();
        for (MenuItem menuItem :
                getMenuItems()) {
            IOSSheetDialog.SheetItem sheetItem = new IOSSheetDialog.SheetItem(menuItem.getText(), menuItem.isCancelled() ? IOSSheetDialog.SheetItem.RED : IOSSheetDialog.SheetItem.BLUE);
            sheetItems.add(sheetItem);
            iosSheetDialog = new IOSSheetDialog.Builder(getActivity())
                    .setTitle(getTitle() != null ? getTitle() : "Menu")
                    .setData(sheetItems, (dialogInterface, i) -> {
                        getMenuListener().itemClick(null, i);
                    })
                    .create();
        }
    }
}
