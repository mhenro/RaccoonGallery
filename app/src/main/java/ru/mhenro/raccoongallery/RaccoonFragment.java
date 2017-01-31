package ru.mhenro.raccoongallery;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhenr on 30.01.2017.
 */

/* class for main fragment */
public class RaccoonFragment extends Fragment {
    private static final String TAG = RaccoonFragment.class.getName();
    private RecyclerView recyclerView;
    private List<GalleryItem> items = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);    //for saving fragment during transitions
        new FetchItemsTask().execute(1);    //getting first json
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_raccoon, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.gallery_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);  //grid with 3 columns
        recyclerView.setLayoutManager(layoutManager);

        /* listener for creating endless list */
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                final int curSize = recyclerView.getAdapter().getItemCount();
                new FetchItemsTask().execute(curSize + 1);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyItemRangeInserted(curSize, items.size() - 1);
                    }
                });
            }
        });
        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            recyclerView.setAdapter(new PhotoAdapter(items));
        }
    }

    /* holder for recyclerview */
    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            itemImageView = (ImageView)itemView.findViewById(R.id.gallery_item);
        }

        public void bindGalleryItem(GalleryItem item) {
            Picasso.with(getActivity())
                    .load(item.getLink())
                    .placeholder(R.drawable.empty)
                    .resize(item.getsWidth(), item.getsHeight())
                    .into(itemImageView);
        }
    }

    /* adapter for recyclerview */
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> items;

        public PhotoAdapter(List<GalleryItem> items) {
            this.items = items;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = items.get(position);
            holder.bindGalleryItem(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    /* asyncTask for getting JSON response in external thread */
    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Integer... page) {
            return new RaccoonFetcher().fetchItems(page[0]);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            boolean isSetupNeeded = true;
            if (items.size() > 0) {
                isSetupNeeded = false;
            }
            items.addAll(galleryItems);
            if (isSetupNeeded) {
                setupAdapter();
            }
        }
    }
}
