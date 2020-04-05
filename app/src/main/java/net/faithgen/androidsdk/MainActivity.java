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
import net.faithgen.sdk.http.FaithGenAPI;
import net.faithgen.sdk.http.types.ServerResponse;
import net.faithgen.sdk.models.Avatar;
import net.faithgen.sdk.models.Date;
import net.faithgen.sdk.models.User;
import net.faithgen.sdk.singletons.GSONSingleton;
import net.faithgen.sdk.utils.Constants;

import java.util.HashMap;

import nouri.in.goodprefslib.GoodPrefs;

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
                        .setItemId("-5-3e4c7f160-b5191986fb06-be8fe23ad2")
                        .setTitle("Sermon title")
                        .setLimit(12)
                        .setCommentsDisplay(CommentsDisplay.ACTIVITY)
                        .build());
/*               new FaithGenAPI(MainActivity.this)
                        .setServerResponse(new ServerResponse() {
                            @Override
                            public void onServerResponse(String serverResponse) {

                            }
                        }).request("");*/
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

        String theUser = "{\n" +
                "    \"success\": true,\n" +
                "    \"message\": \"Login successful.\",\n" +
                "    \"user\": {\n" +
                "        \"id\": \"c2582401-7c7a-404d-9d56-99a754f4ce27\",\n" +
                "        \"active\": true,\n" +
                "        \"name\": \"New Name\",\n" +
                "        \"email\": \"my@email.com\",\n" +
                "        \"phone\": \"0651562691\",\n" +
                "        \"provider\": \"faithgen\",\n" +
                "        \"avatar\": {\n" +
                "            \"_50\": \"http://192.168.8.100:8001/storage/users/50-50/57a54f7286109507444c23-c07955d1988-132959c094-9e1ad8-815.png\",\n" +
                "            \"_100\": \"http://192.168.8.100:8001/storage/users/100-100/57a54f7286109507444c23-c07955d1988-132959c094-9e1ad8-815.png\",\n" +
                "            \"original\": \"http://192.168.8.100:8001/storage/users/original/57a54f7286109507444c23-c07955d1988-132959c094-9e1ad8-815.png\"\n" +
                "        },\n" +
                "        \"joined\": {\n" +
                "            \"approx\": \"2 hours ago\",\n" +
                "            \"formatted\": \"Sat 04 Apr 2020\",\n" +
                "            \"exact\": \"2020-04-04T09:38:28.000000Z\",\n" +
                "            \"time\": \"09:38\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"processing_image\": false\n" +
                "}";

        User user = new User("c2582401-7c7a-404d-9d56-99a754f4ce27", "User Name", new Avatar("","", "", ""), "null", "null", "null", false, true, new Date("", "", "", ""));
        GoodPrefs.getInstance().saveObject(Constants.USER, user);
    }
}
