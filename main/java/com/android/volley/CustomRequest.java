package com.android.volley;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class CustomRequest extends Request<JSONObject> {

	private Listener<JSONObject> listener;
	private Map<String, String> params;
	private boolean authHeader;
	
	public CustomRequest(String url, boolean authHeader, Map<String, String> params,
	        Listener<JSONObject> reponseListener, ErrorListener errorListener) {
	    super(Method.GET, url, errorListener);
	    this.authHeader = authHeader;
	    this.listener = reponseListener;
	    this.params = params;
	}
	
	public CustomRequest(int method, String url, boolean authHeader, Map<String, String> params,
	        Listener<JSONObject> reponseListener, ErrorListener errorListener) {
	    super(method, url, errorListener);
	    this.authHeader = authHeader;
	    this.listener = reponseListener;
	    this.params = params;
	}
	
	protected Map<String, String> getParams()
	        throws AuthFailureError {
	    return params;
	};
	
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
	    try {
	        String jsonString = new String(response.data,
	                HttpHeaderParser.parseCharset(response.headers));
	        return Response.success(new JSONObject(jsonString),
	                HttpHeaderParser.parseCacheHeaders(response));
	    } catch (UnsupportedEncodingException e) {
	        return Response.error(new ParseError(e));
	    } catch (JSONException je) {
	        return Response.error(new ParseError(je));
	    }
	}
	
	@Override
	protected void deliverResponse(JSONObject response) {
	    listener.onResponse(response);
	}
	
	@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
		if (authHeader) {
	       	HashMap<String, String> params = new HashMap<String, String>();
	       	String userpass = "vineet:vineet";
	   		String auth = "Basic " + new String(Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP));
	   		params.put("Authorization", auth);
	   		return params;
		} else {
			return Collections.emptyMap();
		}
    }
}
