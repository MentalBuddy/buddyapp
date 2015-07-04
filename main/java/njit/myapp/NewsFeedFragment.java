package njit.myapp;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import njit.myapp.cache.Cache;
import njit.myapp.cache.NewsFeedPostsResponse;
import njit.myapp.cache.PostDetails;
import njit.myapp.cache.ProfilePostsResponse;
import com.android.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewsFeedFragment extends Fragment implements FetchCompleteListener{

    ListView list = null;
    NewsFeedListAdapter adapter = null;
    ImageView add = null, reload;

    LayoutInflater inflater;

    RequestQueue requestQueue;
    String email = "";

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = ((MainActivity)getActivity()).requestQueue();
        fetchPosts();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    //    Toast.makeText(getActivity(), "pos:  " , Toast.LENGTH_SHORT).show();
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        list = (ListView)view.findViewById(R.id.list);
        add = (ImageView)view.findViewById(R.id.add);
        reload = (ImageView)view.findViewById(R.id.refresh);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                //   inflater.inflate()
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                View view = inflater.inflate(R.layout.my_dialog, null);
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



        adapter = new NewsFeedListAdapter(getActivity());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //
                //   Toast.makeText(getActivity(), "pos:  "+ (position - list.getHeaderViewsCount()) , Toast.LENGTH_SHORT).show();
           //     displayDetailedComment(0);
                ((MainActivity)getActivity()).displayMood(position);
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPosts();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    private void displayDetailedComment(int position) {
        ContextThemeWrapper themedContext;
        themedContext = new ContextThemeWrapper( getActivity(), android.R.style.Holo_Light_ButtonBar_AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


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


        View view = inflater.inflate(R.layout.list_item_expanded, null);
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
    public void fetchPosts() {
        email ="nidhish2k@gmail.com";
        String url = "http://167.114.114.17/newsfeed/index.php?user_id=" + email;

        CustomRequest request = new CustomRequest(Request.Method.GET, url, true, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
        //        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                System.out.println("res nff:  " + response);
                new NewsFeedPostsResponse().
                        parse(NewsFeedFragment.this, response);

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
        request.setTag(getActivity());
        requestQueue.add(request);
    }

    private void createPost(String description, final AlertDialog alertDialog) {

            String url = "http://167.114.114.17/posts/create.php";
            email = "nidhish2k@gmail.com";

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
                    fetchPosts();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
             //       Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("err:  " + error);
                    //    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            request.setTag(getActivity());
            requestQueue.add(request);
    }

    @Override
    public void fetchComplete() {
        adapter.notifyDataSetChanged();
    }
}