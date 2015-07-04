package njit.myapp.cache;

import njit.myapp.FetchCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Nidhish on 18-06-2015.
 */
public class ProfilePostsResponse {
    String creator, time, description, hugs;
    JSONArray  posts, huggedPosts, postIds;

    public ProfilePostsResponse() {
        postIds = new JSONArray();
    }

    public void parse(FetchCompleteListener listener, JSONObject response) {

        try {
            JSONArray orderedkeys = response.getJSONArray("order");
            response.remove("order");

            postIds = orderedkeys;

        //    huggedPosts = response.getJSONArray("hugged");
        //    response.remove("hugged");
        //    Cache.INSTANCE.setPostsHuggedOrder(huggedPosts);
            Cache.INSTANCE.set_profile_posts(orderedkeys);

            for(int i = 0 ; i < orderedkeys.length() ; i++) {
                String key = orderedkeys.getString(i);
                JSONObject tokens = response.getJSONObject(key);
                creator = tokens.getString("creator");
                time = tokens.getString("time");
                description = tokens.getString("description");
                hugs = tokens.getString("hugs");
                PostDetails postdetails = new PostDetails(creator, time, description, hugs);
                Cache.INSTANCE.add_post(key, postdetails);
           }
            listener.fetchComplete();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
