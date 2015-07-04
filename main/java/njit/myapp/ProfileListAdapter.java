package njit.myapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import njit.myapp.cache.Cache;
import njit.myapp.cache.PostDetails;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Nidhish on 11-04-2015.
 */
public class ProfileListAdapter extends BaseAdapter{

    private Context context;
    private Resources res;
    private LayoutInflater inflater = null;

    String name, time, description, hugs;

    private TextView hugs_text;

    ProfileListAdapter(Context c) {
        context = c;
        res = c.getResources();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        //This is where you set the count of items/ elements in the list view.
        int length = 0;
        try {
            length = Cache.INSTANCE.profile_posts_count();
        } catch(NullPointerException exception) {}
        return length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = null;
        getData(position);
        //Use the way below, its for making sure you recycle the views you've just created for an element.
        //The listview will recycle inflated views(items) once they've move off the screen and assign them to the item
        //which needs to be inflated.
        //This helps is avoiding unnecessary memory allocation and proper recycling.

        if (convertView == null) {
            row = inflater.inflate(R.layout.list_item,parent,false);
        //    row.setAnimation(null);
        } else {
            row = convertView;
         //   row.setAnimation(null);
        //    row.clearAnimation();
        //    row.setAnimation(AnimationUtils.loadAnimation(context, R.anim.anim));

        /*    row.animate().withEndAction(new Runnable() {
                @Override
                public void run() {

                }
            })
        view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
        */
        }

        TextView username_text = (TextView) row.findViewById(R.id.username);
        TextView post_text = (TextView) row.findViewById(R.id.comment);
        TextView time_text = (TextView) row.findViewById(R.id.time);
        hugs_text = (TextView) row.findViewById(R.id.hugs);



        hugs_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).hugsDialogBox(position);
            }
        });

        Date date = new Date(Long.valueOf(time)*1000);
        String formattedtime = new SimpleDateFormat("dd MMM H:mm a").format(date);

        post_text.setText(description);
        time_text.setText(formattedtime);
        hugs_text.setText(hugs + " Hugs");
    //    comment_num.setText("Comment No: " + (position + 1));

        return row;
    }

    public void getData(int position) {
        try {
            PostDetails postDetails = Cache.INSTANCE.get_profile_post(position);
            System.out.println(postDetails);
            name = postDetails.getName();
            time = postDetails.getTime();
            description = postDetails.getDescription();
            hugs = postDetails.getHugs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
