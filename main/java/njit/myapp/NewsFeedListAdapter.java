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
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Nidhish on 11-04-2015.
 */
public class NewsFeedListAdapter extends BaseAdapter{

    private Context context;
    private Resources res;
    private LayoutInflater inflater = null;

    private TextView mood_text;

    private ArrayList<String> moods = new ArrayList<>();

    NewsFeedListAdapter(Context c) {
        context = c;
        res = c.getResources();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        moods.add("All");
        moods.add("Rant");
        moods.add("Encourage");
        moods.add("Laugh");
        moods.add("Rage");
        moods.add("Confess");
    }


    @Override
    public int getCount() {
        //This is where you set the count of items/ elements in the list view.
        int length = moods.size();

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
        row = inflater.inflate(R.layout.mood_list_item,parent,false);
        mood_text = (TextView) row.findViewById(R.id.mood);
        mood_text.setText(moods.get(position));
        return row;
    }
}
