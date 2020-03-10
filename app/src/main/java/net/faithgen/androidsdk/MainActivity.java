package net.faithgen.androidsdk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.faithgen.sdk.SDK;
import net.faithgen.sdk.comments.CommentsSettings;
import net.faithgen.sdk.enums.CommentsDisplay;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.textView);
        setSupportActionBar(toolbar);

        params = new HashMap<>();
        params.put("limit", "5");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setTextColor(Color.parseColor(SDK.getThemeColor()));
                HashMap<String, String> params = new HashMap<>();
                params.put("first_char", "this is the params");
                params.put("second params", "2");
                //  API.get(MainActivity.this, "https://theroute", params, false, null);

                SDK.openComments(MainActivity.this, new CommentsSettings.Builder()
                        .setCategory("sermons/")
                        //.setItemId("-5-3e4c7f160-b5191986fb06-be8fe23ad2")
                        .setItemId("79c9-9c01738c296fe3019989a-e167-34e-")
                        .setTitle("Sermon title")
                        .setLimit(2)
                        .setCommentsDisplay(CommentsDisplay.DIALOG)
                        .build());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
