package njit.myapp;

import android.app.ActionBar;
import android.app.Activity;

import android.app.FragmentManager;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import njit.myapp.cache.Cache;
import njit.myapp.cache.PostDetails;
import com.android.volley.CustomRequest;;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    ProfileFragment profileFragment = null;
    NewsFeedFragment newsFeedFragment = null;

//    MoodFragment moodFragment = null;

//  private DiscussionPagerAdapter adapter;
    private CustomPager pager;
//    private ActionBar actionBar;

    private TestPagerAdapter adapter;

//    private FragmentManager manager;
//    private FragmentTransaction transaction;

    private SlidingTabLayout mTabs;

    Toolbar toolbar;
    RequestQueue requestQueue;

    private ArrayList<Integer> selectedUser = new ArrayList<Integer>();
    private String [] positions;

    private boolean[] isSelected = {false, false, false, false, false,
            false, false, false, false, false, false};

    private Bundle bundle = null;

    private String PREFS_NAME = "NJITAppPrefsFile";
    private String SETTINGS_PREFS_NAME = "SettingsPrefsFile";

    EditText user_id;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        editEmailId();
        Cache.INSTANCE.initialize();
        adapter = new TestPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);

        pager = (CustomPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);

        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(pager);
    }

    public RequestQueue requestQueue() {
        return requestQueue;
    }

    public void displayMood(int index) {
        Intent intent = new Intent(MainActivity.this, MoodActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    public void hugsDialogBox(int position) {
        //This is where you create a CharSequesnce of User names who have hugged the user and inflate the multichoice list in a dialog box.

        CharSequence[] hugsBackUserNames = {"User 1","User 2","User 3", "User 4", "User 5", "User 6", "User 7"};


        ContextThemeWrapper themedContext;
        themedContext = new ContextThemeWrapper( MainActivity.this, android.R.style.Holo_Light_ButtonBar_AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //	builder.set

        builder.setTitle("Select Users").setMultiChoiceItems(hugsBackUserNames, isSelected,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            selectedUser.add(Integer.valueOf(which));
                            isSelected[which] = true;
                        } else {
                            selectedUser.remove(Integer.valueOf(which));
                            isSelected[which] = false;
                        }
                    }
                })
                .setPositiveButton("Hug back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //    PositionsSelected(isSelected);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCancel();

                    }
                }).create();

        builder.show();
    }

    public void onCancel() {
    }

    private void editEmailId() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        View view = getLayoutInflater().inflate(R.layout.email_dialog, null);
        dialogBuilder.setView(view);
        final AlertDialog alertDialog = dialogBuilder.create();
        Button post = (Button) view.findViewById(R.id.button_post);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String email = settings.getString("email","nidhish2k@gmail.com");
        user_id = (EditText) view.findViewById(R.id.status);
        user_id.setText(email);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     adapter.addPost(status.getText().toString());
                if (user_id.getText().toString().trim().length() > 0) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("email", user_id.getText().toString().trim().toLowerCase());
                    editor.commit();
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }
}

