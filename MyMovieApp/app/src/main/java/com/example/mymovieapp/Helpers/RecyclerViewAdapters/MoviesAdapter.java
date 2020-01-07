package com.example.mymovieapp.Helpers.RecyclerViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mymovieapp.Helpers.BaseViewHolder;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieResults;
import com.example.mymovieapp.R;
import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<BaseViewHolder>
{
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    
    private ArrayList<MovieResults.Result> mResultList;
    Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoviesAdapter(Context context, ArrayList<MovieResults.Result> list)
    {
        this.context = context;
        this.mResultList = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case VIEW_TYPE_NORMAL:
                return new MovieViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_view, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_loading_view, parent, false));
            default:
                return null;
        }
    }

    /*public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView tv_title;
        public TextView tv_description;
        public ImageView iv_poster;
        public MyViewHolder(View view)
        {
            super(view);
            tv_title =view.findViewById(R.id.tv_title);
            tv_description = view.findViewById(R.id.tv_description);
            iv_poster = view.findViewById(R.id.iv_poster);
        }
    }*/

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position)
    {
        holder.onBind(position);
        //((MovieViewHolder) holder).onBind(position);
    }

//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        // set the data in items
//        Object output = mResultList.get(position);
//        String str_output= output.toString();
//        holder.tv_title.setText(str_output);
//        // implement setOnClickListener event on item view. {...}
//
//}
    @Override
    public int getItemViewType(int position)
    {
        if (isLoaderVisible)
        {
            return position == mResultList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        }
        else
        {
            return VIEW_TYPE_NORMAL;
        }

    }


    @Override
    public int getItemCount()
    {
        return mResultList == null ? 0 : mResultList.size();
    }


    public void addItems(List<MovieResults.Result> postItems)
    {
        mResultList.addAll(postItems);
        notifyDataSetChanged();
    }


    public void addLoading()
    {
        isLoaderVisible = true;
        mResultList.add(new MovieResults.Result());
        notifyItemInserted(mResultList.size() - 1);
    }


    public void removeLoading()
    {
        isLoaderVisible = false;
        int position = mResultList.size() - 1;
        MovieResults.Result item = getItem(position);

        if (item != null)
        {
            mResultList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void clear()
    {
        mResultList.clear();
        notifyDataSetChanged();
    }


    MovieResults.Result getItem(int position)
    {
        try
        {
            return mResultList.get(position);
        }
        catch (Exception e)
        {
            Log.d("exception", "problem in movie adapter");
        }
        return mResultList.get(0);
    }


    public class MovieViewHolder extends BaseViewHolder
    {
        TextView tv_title;
        TextView tv_description;
        ImageView iv_poster;

        MovieViewHolder(View itemView)
        {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            iv_poster = itemView.findViewById(R.id.iv_poster);
        }


        protected void clear() { }


        public void onBind(int position)
        {
            super.onBind(position);
            MovieResults.Result output =  mResultList.get(position);
            // load image
            Glide
                .with(context)
                .load("https://image.tmdb.org/t/p/w200/" + output.getPosterPath())
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .into(iv_poster);
            tv_title.setText(output.getTitle());
            tv_description.setText(output.getOverview());
        }
    }


    public class ProgressHolder extends BaseViewHolder
    {
        ProgressBar progressBar;

        ProgressHolder(View itemView)
        {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }

        @Override
        protected void clear() { }
    }
}
