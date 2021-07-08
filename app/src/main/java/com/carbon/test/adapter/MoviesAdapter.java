package com.carbon.test.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carbon.test.R;
import com.carbon.test.activity.MovieDetailActivity;
import com.carbon.test.common.interfaces.OnLoadMoreListener;
import com.carbon.test.common.interfaces.RecyclerViewItem;
import com.carbon.test.database.model.Movie;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading = false, permitLoadMore = true;
    private final int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private final RecyclerView recyclerView;
    private final RecyclerView.OnScrollListener mOnScrollListener;
    private final Activity activity;
    private final List<RecyclerViewItem> items;

    //Constructor
    public MoviesAdapter(RecyclerView recyclerView, Activity activity, List<RecyclerViewItem> items) {

        this.activity = activity;
        this.items = items;
        this.recyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = Objects.requireNonNull(linearLayoutManager).getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (isPermitLoadMore() && !isLoading() && getItemCount() >= 30
                        && lastVisibleItem >= (totalItemCount - visibleThreshold)) {
                    if (mOnLoadMoreListener != null) mOnLoadMoreListener.onLoadMore();
                }

            }
        };

        this.recyclerView.addOnScrollListener(mOnScrollListener);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewItem item = items.get(position);
        return item != null ? item.getItemType() : -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == RecyclerViewItem.VIEW_TYPE_ITEM) {

            return new ItemViewHolder(
                    LayoutInflater.from(activity).inflate(R.layout.movie_list_layout, parent, false)
            );

        } else if (viewType == RecyclerViewItem.VIEW_TYPE_LOADING) {

            return new LoadingViewHolder(
                    LayoutInflater.from(activity).inflate(R.layout.shimmer_loading_layout, parent, false)
            );

        } else if (viewType == RecyclerViewItem.VIEW_TYPE_LOADING_MORE) {

            return new LoadingMoreViewHolder(
                    LayoutInflater.from(activity).inflate(R.layout.loader, parent, false)
            );

        } else if (viewType == RecyclerViewItem.VIEW_TYPE_NO_MORE_RECORD) {

            return new NoMoreRecordViewHolder(
                    LayoutInflater.from(activity).inflate(R.layout.no_more_record, parent, false)
            );

        } else {

            return new NoContentViewHolder(
                    LayoutInflater.from(activity).inflate(R.layout.no_content_layout, parent, false)
            );

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Movie movie = (Movie) items.get(position);

            Glide.with(activity)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_image_placeholder)
                    .into(itemViewHolder.ivMoviePoster);

            itemViewHolder.tvTitle.setText(movie.getTitle());
            itemViewHolder.tvReleaseDate.setText(String.format("Release Date: %s", movie.getReleaseDate()));
            itemViewHolder.tvVote.setText(String.valueOf(movie.getVote()));
            itemViewHolder.ratingBar.setRating(movie.getVote() / 2);

            itemViewHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                activity.startActivity(intent);
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.shimmerFrameLayout.startShimmer();
        } else if (holder instanceof LoadingMoreViewHolder) {
            LoadingMoreViewHolder loadingViewHolder = (LoadingMoreViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        } else if (holder instanceof NoMoreRecordViewHolder) {
            NoMoreRecordViewHolder noMoreRecordViewHolder = (NoMoreRecordViewHolder) holder;
            noMoreRecordViewHolder.tvNoMoreRecord.setVisibility(View.VISIBLE);
        } else if (holder instanceof NoContentViewHolder) {
            NoContentViewHolder noContentViewHolder = (NoContentViewHolder) holder;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = getDip(100);
            noContentViewHolder.itemView.setLayoutParams(params);
            noContentViewHolder.title.setText(R.string.no_movie);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public boolean hasItem() {
        return hasItem(this.items);
    }

    public boolean hasItem(List<RecyclerViewItem> items) {
        if (items == null) return false;
        boolean hasItem = false;
        for (RecyclerViewItem item : items) {
            if (item != null && item.getItemType() == RecyclerViewItem.VIEW_TYPE_ITEM) {
                hasItem = true;
                break;
            }
        }
        return hasItem;
    }

    public boolean isPermitLoadMore() {
        return permitLoadMore;
    }

    public void setPermitLoadMore(boolean permitLoadMore) {
        this.permitLoadMore = permitLoadMore;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(final boolean loaded) {

        if (loaded) {
            recyclerView.clearOnScrollListeners();
        } else if (isLoading()) {
            recyclerView.addOnScrollListener(mOnScrollListener);
        }
        isLoading = loaded;
    }

    public void removeAllViewItemExceptHeaderAnd(int itemType) {
        List<RecyclerViewItem> list = new ArrayList<>();
        for (RecyclerViewItem item : items) {
            if (item == null) continue;
            if (item.getItemType() == RecyclerViewItem.VIEW_TYPE_HEADER) list.add(item);
            if (item.getItemType() == itemType) list.add(item);
        }
        items.clear();
        items.addAll(list);
    }

    public void removeAllViewItems() {
        items.clear();
    }

    public void removeAllViewItemExcept(int itemType) {
        List<RecyclerViewItem> list = new ArrayList<>();
        for (RecyclerViewItem item : items) {
            if (item == null) continue;
            if (item.getItemType() == itemType) list.add(item);
        }
        items.clear();
        items.addAll(list);
    }

    public void removeViewItem(int itemType) {
        List<RecyclerViewItem> list = new ArrayList<>();
        for (RecyclerViewItem item : items) {
            if (item == null) continue;
            if (item.getItemType() != itemType) list.add(item);
        }
        items.clear();
        items.addAll(list);
    }

    public int getDip(int dip) {
        float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {

        private final ShimmerFrameLayout shimmerFrameLayout;

        public LoadingViewHolder(View view) {
            super(view);
            shimmerFrameLayout = view.findViewById(R.id.shimmer_view);
        }
    }

    private static class LoadingMoreViewHolder extends RecyclerView.ViewHolder {
        public final ProgressBar progressBar;

        public LoadingMoreViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final ImageView ivMoviePoster;
        public final TextView tvTitle, tvReleaseDate, tvVote;
        public final RatingBar ratingBar;

        public ItemViewHolder(View view) {
            super(view);
            ivMoviePoster = view.findViewById(R.id.movie_poster);
            tvTitle = view.findViewById(R.id.title);
            tvReleaseDate = view.findViewById(R.id.release_date);
            tvVote = view.findViewById(R.id.vote);
            ratingBar = view.findViewById(R.id.ratingBar);
        }
    }

    private static class NoMoreRecordViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvNoMoreRecord;

        public NoMoreRecordViewHolder(View view) {
            super(view);
            tvNoMoreRecord = view.findViewById(R.id.no_more_record);
        }
    }

    private static class NoContentViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public NoContentViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }
}
