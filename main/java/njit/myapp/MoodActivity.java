package njit.myapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import njit.myapp.cache.Cache;
import njit.myapp.cache.NewsFeedPostsResponse;
import njit.myapp.cache.PostDetails;
import com.android.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//import android.app.Fragment;

public class MoodActivity extends ActionBarActivity implements FetchCompleteListener{

    ListView list = null;
    MoodsListAdapter adapter = null;
    ImageView add = null, reload;

//    LayoutInflater inflater;

    RequestQueue requestQueue;
    String email = "";
//    String title = "";
    int mood = 0;

    private String PREFS_NAME = "NJITAppPrefsFile";

    TextView user_id;

    Toolbar toolbar;


    public MoodActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        requestQueue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        mood = bundle.getInt("index");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView)findViewById(R.id.list);
        add = (ImageView)findViewById(R.id.add);
        reload = (ImageView)findViewById(R.id.refresh);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                //   inflater.inflate()
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MoodActivity.this);

                View view = getLayoutInflater().inflate(R.layout.my_dialog, null);
                dialogBuilder.setView(view);
                final AlertDialog alertDialog = dialogBuilder.create();
                Button post = (Button) view.findViewById(R.id.button_post);
                final EditText status = (EditText) view.findViewById(R.id.status);
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //        adapter.addPost(status.getText().toString());
                        createPost(status.getText().toString(), alertDialog);
                        //        list.setSelection(adapter.getCount());
                        //        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });



        adapter = new MoodsListAdapter(this);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //
                //   Toast.makeText(getActivity(), "pos:  "+ (position - list.getHeaderViewsCount()) , Toast.LENGTH_SHORT).show();
                displayDetailedComment(0);
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPosts(mood);
            }
        });

        fetchPosts(mood);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    private void displayDetailedComment(int position) {
        ContextThemeWrapper themedContext;
        themedContext = new ContextThemeWrapper(this, android.R.style.Holo_Light_ButtonBar_AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(MoodActivity.this);


        int actual_position = 0;
        View list_element = list.getChildAt(actual_position);
        TextView username = (TextView) list_element.findViewById(R.id.username);
        TextView comment = (TextView) list_element.findViewById(R.id.comment);
        TextView time = (TextView) list_element.findViewById(R.id.time);

        String name_string = "Username";
        String comment_string = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                "ut aliquip ex ea commodo consequat." +
                " Duis aute irure dolor in reprehenderit in voluptate " +
                "velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa" +
                " qui officia deserunt mollit anim id est laborum.";


        String time_string = "2 mins ago";


        View view = getLayoutInflater().inflate(R.layout.list_item_expanded, null);
        TextView detailed_username = (TextView) view.findViewById(R.id.username);
        TextView detailed_comment = (TextView) view.findViewById(R.id.comment);
        TextView detailed_time = (TextView) view.findViewById(R.id.time);

        detailed_username.setText(name_string);
        detailed_comment.setText(comment_string);
        detailed_time.setText(time_string);


        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();
/*

        builder.setTitle(title).setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).
                setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();
*/    }
    public void fetchPosts(int index) {
        this.mood = index;
        String mood = "";
        switch (index){
            case 0:
                mood = "All";
                break;
            case 1:
                mood = "Rant";
                break;
            case 2:
                mood = "Encourage";
                break;
            case 3:
                mood = "Laugh";
                break;
            case 4:
                mood = "Rage";
                break;
            case 5:
                mood = "Confess";
                break;
            default:
                mood = "All";
                break;
        }

        getSupportActionBar().setTitle(mood);

        SharedPreferences settings = MoodActivity.this.getSharedPreferences(PREFS_NAME, 0);
        email = settings.getString("email","nidhish2k@gmail.com");
        String url = "http://167.114.114.17/newsfeed/index.php?user_id=" + email + "&mood="+ mood;
        System.out.println("mood act fetch post url:  " + url);
        CustomRequest request = new CustomRequest(Request.Method.GET, url, true, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
        //        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

         //       System.out.println("res nff:  " + response);
                new NewsFeedPostsResponse().
                        parse(MoodActivity.this, response);

                //    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
         //       Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("err:  " + error);
                //    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setTag(MoodActivity.this);
        requestQueue.add(request);
    }

    private void createPost(String description, final AlertDialog alertDialog) {

            String url = "http://167.114.114.17/posts/create.php";
            email = "nidhish2k@gmail.com";
        SharedPreferences settings = MoodActivity.this.getSharedPreferences(PREFS_NAME, 0);
        email = settings.getString("email","nidhish2k@gmail.com");
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", email);
            params.put("description", description);

        System.out.println("params:  " + params);
            CustomRequest request = new CustomRequest(Request.Method.POST, url, true, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
              //      Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("res:  " + response);
                    //    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    list.setSelection(adapter.getCount());
                    alertDialog.dismiss();
                    fetchPosts(mood);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
             //       Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("err:  " + error);
                    //    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            request.setTag(MoodActivity.this);
            requestQueue.add(request);
    }


    public void hugPostProfile(String postId) {
        String email = "nidhish2k@gmail.com";
        String url = "http://167.114.114.17/hugs/create.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("post_id", postId);
        params.put("user_id", email);


        CustomRequest request = new CustomRequest(Request.Method.POST, url, true, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("res:  " + response);
                try {
                    String post_id = response.getString("post_id");
                    String name = response.getString("creator");
                    String time = response.getString("time");
                    String description = response.getString("description");
                    String hugs = response.getString("hugs");
                    PostDetails post = new PostDetails(name, time, description,hugs);
                    Cache.INSTANCE.add_post(post_id, post);
                    fetchComplete();
                    //    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("err:  " + error);
                //    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setTag(MoodActivity.this);
        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void fetchComplete() {
        adapter.notifyDataSetChanged();
    }
}