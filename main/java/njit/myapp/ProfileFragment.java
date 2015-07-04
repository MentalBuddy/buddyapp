package njit.myapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import njit.myapp.cache.Cache;
import njit.myapp.cache.ProfilePostsResponse;
import com.android.volley.CustomRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment implements  FetchCompleteListener {

    ListView list = null;
    ProfileListAdapter adapter = null;
    ImageView add = null;

    private Button edit_profile_description = null;
    private TextView profile_description = null;

    private Button edit_profile_dob = null;
    private Button edit_profile_gender = null;

    private Button edit_profile_sexuality = null;
    private Button edit_profile_race = null;

    private TextView profile_dob = null;
    private TextView profile_gender = null;
    private TextView profile_sexuality = null;
    private TextView profile_race = null;
    private LayoutInflater inflater;

    private String PREFS_NAME = "NJITAppPrefsFile";

    RequestQueue requestQueue;
    String email, dob, gender, race, sexuality, description;

    private Button done;
    //private Vector <String> hugBackUserNames;

    private String mood = "";
    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = ((MainActivity)getActivity()).requestQueue();

        fetchProfile();
        fetchPosts();
        if (getArguments() != null) {
        }
    }


    public  static ProfileFragment getInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return  profileFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        list = (ListView)view.findViewById(R.id.list);
        add = (ImageView)view.findViewById(R.id.add);

    //    hugBackUserNames = new CharSequence();
        designDrawable();
        adapter = new ProfileListAdapter(getActivity());
        list.setAdapter(adapter);

        View header = inflater.inflate(R.layout.profile_list_header,list,false);

        edit_profile_description = (Button) header.findViewById(R.id.profile_description_edit_button);
        profile_description = (TextView) header.findViewById(R.id.profile_description);
        profile_dob = (TextView) header.findViewById(R.id.profile_dob_value);

        edit_profile_sexuality = (Button)header.findViewById(R.id.profile_sexuality_edit_button);
        edit_profile_dob = (Button) header.findViewById(R.id.profile_dob_edit_button);

        edit_profile_race = (Button) header.findViewById(R.id.profile_race_edit_button);
        edit_profile_gender = (Button) header.findViewById(R.id.profile_gender_edit_button);

        profile_gender = (TextView) header.findViewById(R.id.profile_gender_value);
        profile_sexuality = (TextView) header.findViewById(R.id.profile_sexuality_value);
        profile_race = (TextView) header.findViewById(R.id.profile_race_value);



        done = (Button) header.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        edit_profile_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDescription();
            }
        });

        edit_profile_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOptions(0);
            }
        });

        edit_profile_race.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOptions(1);
            }
        });

        edit_profile_sexuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOptions(2);
            }
        });

        edit_profile_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                profile_dob.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                dob = dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year;
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        list.addHeaderView(header, null, false);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int left_to_display = totalItemCount - (firstVisibleItem + visibleItemCount);
                if (left_to_display <= 10) {
                    //   adapter.setCount(totalItemCount + 10);
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodSelectionDialog();
                //
                //   inflater.inflate()
            }
        });

/*
        add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                //        x = event.getX();
                 //       y = event.getY();
                 //       dx = x - myView.getX();
                 //       dy = y - myView.getY();
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {
                 //       myView.setX(event.getX() - dx);
                 //       myView.setY(event.getY() - dy);
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        //your stuff
                    }
                    return true;
                }
            }
        });
*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                displayDetailedComment(0);
            }
        });

/*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {
                view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setAlpha(1);
                    }
                });

            }
        });

*/
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                view.setBackgroundResource(R.drawable.selected_curved_corners);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        float mdownX;
        private int mSwipeSlop = -1;
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return false;
        }
    };

    private void designDrawable() {

        Drawable tc_drawable = getActivity().getResources().getDrawable(R.drawable.add_default);
        Drawable pressed_drawable = getActivity().getResources().getDrawable(R.drawable.add);
        pressed_drawable.setColorFilter(0x77ff0000,PorterDuff.Mode.SRC_ATOP);
        StateListDrawable stateDrawable = new StateListDrawable();
        stateDrawable.addState(new int[] {android.R.attr.state_activated },
                getActivity().getResources().getDrawable(R.drawable.add_default));
        stateDrawable.addState(new int[] {android.R.attr.state_pressed},
                pressed_drawable);
        stateDrawable.addState(new int[]{},
                tc_drawable);
        add.setBackgroundDrawable(stateDrawable);
    }


    private void displayOptions(final int choice) {
        ContextThemeWrapper themedContext;
        themedContext = new ContextThemeWrapper( getActivity(), android.R.style.Holo_Light_ButtonBar_AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String title = "";
        CharSequence [] choices = null;
        final List<String> listItems = new ArrayList<String>();
     //   TextView set_value = null;

        switch (choice) {
            case 0:
     //           set_value = profile_gender;
                title = "Gender";
                choices = new CharSequence[6];
                listItems.clear();
                listItems.add("Male");
                listItems.add("Female");
                listItems.add("Trans");
                listItems.add("Gender Fluid");
                listItems.add("Gender neutral");
                listItems.add("Its complicated");
                choices = listItems.toArray(new CharSequence[listItems.size()]);
                break;
            case 1:
        //        set_value = profile_race;
                title = "Race";
                choices = new CharSequence[6];
                listItems.clear();
                listItems.add("African American/Black");
                listItems.add("Hispanic/Latin");
                listItems.add("Asian");
                listItems.add("Native American/Alaskan");
                listItems.add("Middle eastern");
                listItems.add("Other");
                choices = listItems.toArray(new CharSequence[listItems.size()]);
                break;
           case 2:
        //       set_value = profile_sexuality;
                title = "Sexuality";
                choices = new CharSequence[6];
                listItems.clear();
                listItems.add("Heterosexual");
                listItems.add("Homosexual");
                listItems.add("Bisexual");
                listItems.add("Pansexual");
                listItems.add("Asexual");
                listItems.add("Prefer not to answer");
                choices = listItems.toArray(new CharSequence[listItems.size()]);
                break;
        /*    case 3:
                title = "Sexuality";
                choices = new CharSequence[6];
                listItems.clear();
                listItems.add("Heterosexual");
                listItems.add("Homosexual");
                listItems.add("Bisexual");
                listItems.add("Pansexual");
                listItems.add("Asexual");
                listItems.add("Prefer not to answer");
                choices = listItems.toArray(new CharSequence[listItems.size()]);
                break;
         */
        }

        builder.setTitle(title).setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (choice) {
                    case 0:
                           profile_gender.setText(listItems.get(which));
                           gender = listItems.get(which);
                        break;
                    case 1:
                           profile_race.setText(listItems.get(which));
                            race = listItems.get(which);
                        break;
                    case 2:
                           profile_sexuality.setText(listItems.get(which));
                        sexuality = listItems.get(which);
                        break;
                }
            }
        }).
        setPositiveButton("Submit", new DialogInterface.OnClickListener() {

           @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        builder.show();
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


    private void updateProfile() {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        email = settings.getString("email","nidhish2k@gmail.com");
        String url = "http://167.114.114.17/profile/create.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id", email);
        params.put("name", "Nidhish Dave");
        params.put("description", description);
        params.put("dob", dob);
        params.put("race", race);
        params.put("sexuality", sexuality);
        params.put("gender", gender);


        System.out.println("params:  " + params);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, true, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
        //        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("res:  " + response);
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

    private void fetchProfile() {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        email = settings.getString("email","nidhish2k@gmail.com");
        String url = "http://167.114.114.17/profile/index.php?user_id="+ email;
        CustomRequest request = new CustomRequest(Request.Method.GET, url, true, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("res:  " + response);
        //        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if(response.has("valid") && response.getString("valid").equals("1")) {
                        JSONObject profile_details = response.getJSONObject("profile_details");
                    //    System.out.println(profile_details.toString());
                        String name = profile_details.getString("name");
                         description = profile_details.getString("description");
                         dob  = profile_details.getString("dob");
                         race = profile_details.getString("race");
                         gender = profile_details.getString("gender");
                         sexuality = profile_details.getString("sexuality");

                        profile_description.setText(description);
                        profile_dob.setText(dob);
                        profile_gender.setText(gender);
                        profile_race.setText(race);
                        profile_sexuality.setText(sexuality);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("err:  " + error);
        //            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setTag(getActivity());
        requestQueue.add(request);
    }


    private void editDescription() {
        ContextThemeWrapper themedContext;
        themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Holo_Light_ButtonBar_AlertDialog);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String description_string = profile_description.getText().toString();

        View view = inflater.inflate(R.layout.profile_list_header_expanded, null);

        final TextView detailed_description = (TextView) view.findViewById(R.id.profile_description_expanded);
        Button done = (Button) view.findViewById(R.id.done_description_editing);
        detailed_description.setText(description_string);
        detailed_description.setEnabled(true);
        detailed_description.requestFocus();

        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = detailed_description.getText().toString();

                profile_description.setText(description);
                dialog.dismiss();
            }
        });
    }


    private void createPost(String mood, String description, final AlertDialog alertDialog) {

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        email = settings.getString("email","nidhish2k@gmail.com");
        String url = "http://167.114.114.17/posts/create.php";
        System.out.println("sharedprefs:  " + settings.getAll().toString());

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id", email.toString());
        params.put("description", description);
        params.put("mood", mood);


        System.out.println("params:  " + params);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, true, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
         //       Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("res:  " + response);
                //    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                list.setSelection(adapter.getCount());
                alertDialog.dismiss();
                fetchPosts();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("err:  " + error);
                //    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setTag(getActivity());
        requestQueue.add(request);
    }

    private void fetchPosts() {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        email = settings.getString("email","nidhish2k@gmail.com");
        String url = "http://167.114.114.17/posts/index.php?user_id=" + email;

        CustomRequest request = new CustomRequest(Request.Method.GET, url, true, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
        //        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                System.out.println("res:  " + response);

                new ProfilePostsResponse().
                        parse(ProfileFragment.this, response);

                //    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("err:  " + error);
                //    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setTag(getActivity());
        requestQueue.add(request);
    }

    private void moodSelectionDialog() {
        final CharSequence[] moods = {"Rant", "Encourage", "Laugh", "Rage", "Confess"};
        ContextThemeWrapper themedContext;
        themedContext = new ContextThemeWrapper( getActivity(), android.R.style.Holo_Light_ButtonBar_AlertDialog);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("How do you feel?").
                setCancelable(true).
                setSingleChoiceItems(moods, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int item) {
                    mood = moods[item].toString();
                    createPostDialog(mood);
                    dialogInterface.dismiss();

            }
        });
        builder.create().show();
    }

    private void createPostDialog(final String mood) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.my_dialog, null);
        dialogBuilder.setView(view);
        final AlertDialog alertDialog = dialogBuilder.create();
        Button post = (Button) view.findViewById(R.id.button_post);
        final EditText status = (EditText) view.findViewById(R.id.status);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     adapter.addPost(status.getText().toString());
                if (status.getText().toString().trim().length() > 0) {
                    createPost(mood, status.getText().toString().trim(), alertDialog);
                }
            }
        });
        alertDialog.show();
    }


    @Override
    public void fetchComplete() {
        adapter.notifyDataSetChanged();
    }
}