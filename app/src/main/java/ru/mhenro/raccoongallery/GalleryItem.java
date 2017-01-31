package ru.mhenro.raccoongallery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mhenr on 30.01.2017.
 */

public class GalleryItem {
    private String caption;
    private String link;
    private int width;
    private int height;
    private String sLink;   //small image
    private int sWidth;
    private int sHeight;

    public GalleryItem() {

    }

    public GalleryItem(JSONObject object) throws JSONException {
        this.caption = object.getString("snippet");
        this.link = object.getString("link");
        JSONObject imageProps = object.getJSONObject("image");
        this.width = imageProps.getInt("width");
        this.height = imageProps.getInt("height");
        this.sLink = imageProps.getString("thumbnailLink");
        this.sWidth = imageProps.getInt("thumbnailWidth");
        this.sHeight = imageProps.getInt("thumbnailHeight");
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getsLink() {
        return sLink;
    }

    public void setsLink(String sLink) {
        this.sLink = sLink;
    }

    public int getsWidth() {
        return sWidth;
    }

    public void setsWidth(int sWidth) {
        this.sWidth = sWidth;
    }

    public int getsHeight() {
        return sHeight;
    }

    public void setsHeight(int sHeight) {
        this.sHeight = sHeight;
    }

    @Override
    public String toString() {
        return caption;
    }
}
