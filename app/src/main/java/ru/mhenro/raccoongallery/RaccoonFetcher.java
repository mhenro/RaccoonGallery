package ru.mhenro.raccoongallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mhenr on 30.01.2017.
 */

/* class for fetching reccoons from rest endpoint */
public class RaccoonFetcher {
    private static final String TAG = RaccoonFetcher.class.getName();
    private  static final String ENDPOINT = "https://www.googleapis.com/customsearch/v1";
    private static final String API_KEY = "AIzaSyAUgqdWM9L0SOhr_J4lKY0bdddTcC8aVRY";
    private static final String CX = "010377822528404984044:bzeevirrlpy";


    /* creating gallery items from json response */
    public List<GalleryItem> fetchItems(final int start) {
        List<GalleryItem> items = new ArrayList<>();
        try {
            String url = Uri.parse(ENDPOINT)
                    .buildUpon()
                    .appendQueryParameter("key", API_KEY)
                    .appendQueryParameter("cx", CX)
                    .appendQueryParameter("q", "raccoons")
                    .appendQueryParameter("searchType", "image")
                    .appendQueryParameter("imgType", "photo")
                    .appendQueryParameter("start", String.valueOf(start))
                    .appendQueryParameter("alt", "json")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.d(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
        return items;
    }

    /* parsing json response and creating gallery items */
    protected void parseItems(List<GalleryItem> items, JSONObject body) throws IOException, JSONException {
        JSONArray itemsArray = body.getJSONArray("items");
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject object = itemsArray.getJSONObject(i);
            GalleryItem item = new GalleryItem(object);
            items.add(item);
        }
    }

    /* function for connecting to REST endpoint */
    public byte[] getUrlBytes(final String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return  out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /* returning json string */
    public String getUrlString(final String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }
}
