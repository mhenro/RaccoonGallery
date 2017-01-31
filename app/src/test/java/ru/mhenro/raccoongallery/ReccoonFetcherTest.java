package ru.mhenro.raccoongallery;

import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.R.attr.path;
import static org.junit.Assert.assertEquals;

/**
 * Created by mhenr on 31.01.2017.
 */

public class ReccoonFetcherTest extends RaccoonFetcher{
    private static final int ITEM_COUNT = 10;

    @Test
    public void parseItemsTest() throws  Exception {
        List<GalleryItem> items = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("{items: [");
        for (int i = 0; i < ITEM_COUNT; i++) {
            sb.append("{snippet:\"image" + i + "\", link:\"http://link.ru\", image: {width: 1024, height: 2048, thumbnailLink: \"http://small.ru\", thumbnailWidth: 128, thumbnailHeight: 128}},");
        }
        sb.append("]}");
        JSONObject jsonBody = new JSONObject(sb.toString());
        parseItems(items, jsonBody);

        assertEquals(ITEM_COUNT, items.size());
    }
}
