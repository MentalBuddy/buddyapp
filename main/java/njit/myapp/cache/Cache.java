package njit.myapp.cache;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Nidhish on 18-06-2015.
 */
public enum Cache {

    INSTANCE;

    HashMap<String, PostDetails> posts = null;


    JSONArray profile_posts_order = null;
    JSONArray newsfeed_posts_order = null;
    JSONArray posts_hugged_order = null;

    public void initialize() {
        profile_posts_order = new JSONArray();
        newsfeed_posts_order = new JSONArray();
        posts = new HashMap<String,  PostDetails>();
    }

    public void clearCache() {
        posts.clear();

        profile_posts_order = null;
        newsfeed_posts_order = null;
        posts_hugged_order = null;

    }

	/*  Posts specific methods */

    public void set_profile_posts(JSONArray posts) {
        profile_posts_order = posts;
    }

    public void set_newsfeed_posts(JSONArray posts) {
        newsfeed_posts_order = posts;
    }

    public void add_post(String post_id, PostDetails post) {
        posts.put(post_id, post);
    }

    public PostDetails get_profile_post(int position){
        PostDetails post = null;
        try {
            String post_id = profile_posts_order.getString(position);

            post = posts.get(post_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return post;
    }

    public PostDetails get_newsfeed_post(int position){
        PostDetails post = null;
        try {
            String post_id = newsfeed_posts_order.getString(position);
            post = posts.get(post_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return post;
    }

    public String getNewsFeedPostId (int position) {
        String post_id = "";
        try {
            post_id = newsfeed_posts_order.getString(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return post_id;
    }



    public int profile_posts_count() {
        return profile_posts_order.length();
    }

    public int newsfeed_posts_count() {
        return newsfeed_posts_order.length();
    }

    public void setPostsHuggedOrder(JSONArray hugged_posts) {
        posts_hugged_order = hugged_posts;
    }

    public String getPostHuggedStatus(int position) {
        String likedStatus = null;
        try {
            if(!posts_hugged_order.isNull(position)) {
                likedStatus = posts_hugged_order.getString(position);
            }
        } catch (JSONException e) {
            //	e.printStackTrace();
        }
        return likedStatus;
    }

    public  void clearMap() {
        posts.clear();
        newsfeed_posts_order = null;
        profile_posts_order = null;
    }
/*
    public String get_all_posts_post(int position) {
        String postName = "";
        try {
            postName = all_posts_posts_order.getString(position);
        } catch (JSONException e) {
        }
        return postName;
    }
*/
}
