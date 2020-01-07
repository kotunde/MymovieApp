package com.example.mymovieapp.Helpers.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mymovieapp.Helpers.BaseViewHolder;
import com.example.mymovieapp.Models.MovieModels.FavouriteMovie;
import com.example.mymovieapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FavouriteMoviesAdapter extends RecyclerView.Adapter<BaseViewHolder>
{
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private ArrayList<FavouriteMovie> mResultList;
    Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FavouriteMoviesAdapter(Context context, ArrayList<FavouriteMovie> list)
    {
        this.context = context;
        this.mResultList = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position)
    {
        holder.onBind(position);
    }


    @Override
    public int getItemCount()
    {
        return mResultList == null ? 0 : mResultList.size();
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
            FavouriteMovie output =  mResultList.get(position);
            iv_poster.setImageBitmap(BitmapFactory.decodeByteArray(output.getImage(),0,output.getImage().length));
            tv_title.setText(output.getTitle());
            tv_description.setText(output.getDescription());
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
