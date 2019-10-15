package net.faithgen.sdk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import net.innoflash.iosview.Toolbar;

/**
 * The root view to be used in the FaithGen app
 * When you want to control the option icon response you should CALL not override the {@link #setOnOptionsClicked(Toolbar.OptionsClicked)}
 */
public abstract class FaithGenActivity extends AppCompatActivity implements Toolbar.OptionsClicked {

    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private Toolbar.OptionsClicked onOptionsClicked;
    private View rootView;
    private View view;

    public abstract String getPageTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_faith_gen, null);
        toolbar = rootView.findViewById(R.id.toolbar);
        constraintLayout = rootView.findViewById(R.id.constraintLayout);

        toolbar.setPageTitle(getPageTitle());
        toolbar.setOnOptionsClicked(this);
        if (SDK.getThemeColor() != null)
            toolbar.getBackButton().setTextColor(Color.parseColor(SDK.getThemeColor()));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Load the page content without the toolbar because its already initialized in the parent
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        view = getLayoutInflater().inflate(layoutResID, null);
        constraintLayout.addView(view);
        super.setContentView(rootView);
    }

    /**
     * Returns the click listener
     *
     * @return
     */
    public Toolbar.OptionsClicked getOnOptionsClicked() {
        return onOptionsClicked;
    }

    /**
     * This makes the option icon available and implements the callback on the interface
     *
     * @param onOptionsClicked
     */
    protected void setOnOptionsClicked(Toolbar.OptionsClicked onOptionsClicked) {
        this.onOptionsClicked = onOptionsClicked;
        if (onOptionsClicked != null) {
            toolbar.setHasOptions(true);
        }
    }

    /**
     * Calls {@link #setOnOptionsClicked(Toolbar.OptionsClicked)} but with a custom options icon
     *
     * @param icon
     * @param onOptionsClicked
     */
    public void setOnOptionsClicked(int icon, Toolbar.OptionsClicked onOptionsClicked) {
        this.onOptionsClicked = onOptionsClicked;
        toolbar.setIcon(icon);
        setOnOptionsClicked(onOptionsClicked);
    }

    @Override
    public void optionsClicked(View view) {
        if (onOptionsClicked != null)
            onOptionsClicked.optionsClicked(view);
    }
/*
    public void optionClicked() {
        if (optionClicked != null) {
            toolbar.setHasOptions(true);
            optionClicked.optionsClicked(toolbar);
        }
    }

    public void optionsClicked(int icon) {

        optionClicked();
    }*/
}
