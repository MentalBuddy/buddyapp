package njit.myapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Nidhish on 11-04-2015.
 */
public class ListAdapter2 extends BaseAdapter{

    private Context context;
    private Resources res;
    private LayoutInflater inflater = null;

    private int count = 10;
    Vector<String> posts = new Vector<>();
    Vector <String> post_time = new Vector<>();
    Vector <Integer> hugs_count = new Vector<>();

    TextView hugs, cancel;

    ListAdapter2(Context c) {
        context = c;
        res = c.getResources();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        //This is where you set the count of items/ elements in the list view.
        count = posts.size();
        return count;
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
            row.clearAnimation();
            row.setAnimation(AnimationUtils.loadAnimation(context, R.anim.anim));

        /*    row.animate().withEndAction(new Runnable() {
                @Override
                public void run() {

                }
            })
        view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
        */
        }

        TextView username = (TextView) row.findViewById(R.id.username);
        TextView comment = (TextView) row.findViewById(R.id.comment);
        final TextView time = (TextView) row.findViewById(R.id.time);
        hugs = (TextView) row.findViewById(R.id.hugs);
        cancel = (TextView) row.findViewById(R.id.cancel);

        hugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hugs_increment = hugs_count.get(position);
                hugs_increment = hugs_increment +1;
                hugs_count.add(position, hugs_increment);
                hugs.setText(hugs_count.get(position) + " Hugs");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hugs_increment = hugs_count.get(position);
                hugs_increment = hugs_increment + 1;
                hugs_count.add(position, hugs_increment);
                hugs.setText(hugs_count.get(position) + " Hugs");

                posts.remove(position);
                hugs_count.remove(position);
                post_time.remove(position);
                ListAdapter2.this.notifyDataSetChanged();
            }
        });


        comment.setText(posts.get(position));
        time.setText(post_time.get(position));
        hugs.setText(hugs_count.get(position) + " Hugs");
        return row;
    }

    public void setCount(int count) {
        this.count = count;
        this.notifyDataSetChanged();
    }

    public void addPost (String post_text) {
        count = count +1;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd MMM");
        String currentDateandTime = sdf.format(new Date());
        posts.add(post_text);
        post_time.add(currentDateandTime);
        hugs_count.add(0);
        this.notifyDataSetChanged();
    }
}
