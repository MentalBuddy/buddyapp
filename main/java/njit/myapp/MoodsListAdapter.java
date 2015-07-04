package njit.myapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import njit.myapp.cache.Cache;
import njit.myapp.cache.PostDetails;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nidhish on 11-04-2015.
 */
public class MoodsListAdapter extends BaseAdapter{

    private Context context;
    private Resources res;
    private LayoutInflater inflater = null;

    String name, time, description, hugs;

    private TextView hugs_text;


    JSONArray posts, huggedPosts, postIds;



    TextView cancel;

    MoodsListAdapter(Context c) {
        context = c;
        res = c.getResources();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        //This is where you set the count of items/ elements in the list view.
        int length = 0;
        try {
            length = Cache.INSTANCE.newsfeed_posts_count();
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
            row = inflater.inflate(R.layout.list_item2,parent,false);
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
        cancel = (TextView) row.findViewById(R.id.cancel);

        hugs_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_id = Cache.INSTANCE.getNewsFeedPostId(position);
                ((MoodActivity)context).hugPostProfile(post_id);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    int hugs_increment = hugs_count.get(position);
                hugs_increment = hugs_increment + 1;
                hugs_count.add(position, hugs_increment);
                hugs.setText(hugs_count.get(position) + " Hugs");

                posts.remove(position);
                hugs_count.remove(position);
                post_time.remove(position);
                NewsFeedListAdapter.this.notifyDataSetChanged();
            */}
        });


        Date date = new Date(Long.valueOf(time)*1000);
        String formattedtime = new SimpleDateFormat("dd MMM H:mm a").format(date);

        post_text.setText(description);
        time_text.setText(formattedtime);
        hugs_text.setText(hugs + " Hugs");
        return row;
    }
/*
    public void setCount(int count) {
        this.count = count;
        this.notifyDataSetChanged();
    }
/*
    public void addPost (String post_text) {
        count = count +1;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd MMM");
        String currentDateandTime = sdf.format(new Date());
        posts.add(post_text);
        post_time.add(currentDateandTime);
        hugs_count.add(0);
        this.notifyDataSetChanged();
    }
*/
    public void getData(int position) {
        try {
            PostDetails postDetails = Cache.INSTANCE.get_newsfeed_post(position);
            name = postDetails.getName();
            time = postDetails.getTime();
            description = postDetails.getDescription();
            hugs = postDetails.getHugs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
