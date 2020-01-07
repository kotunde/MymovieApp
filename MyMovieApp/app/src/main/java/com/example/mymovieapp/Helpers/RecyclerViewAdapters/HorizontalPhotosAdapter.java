package com.example.mymovieapp.Helpers.RecyclerViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieImages;
import com.example.mymovieapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalPhotosAdapter extends RecyclerView.Adapter<HorizontalPhotosAdapter.MyViewHolder>
{

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MovieImages.Poster> moviePosters;


    public HorizontalPhotosAdapter(Context context, List<MovieImages.Poster> temp_movie_posters)
    {
        //Log.d("debug","HorizontalPhotosAdapter");
        this.moviePosters = (ArrayList<MovieImages.Poster>) temp_movie_posters;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView riv;

        public MyViewHolder(View view)
        {
            super(view);

            riv = view.findViewById(R.id.horizontal_item_view_image);

        }
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //Log.d("debug","onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);

        if (itemView.getLayoutParams ().width == RecyclerView.LayoutParams.MATCH_PARENT)
            itemView.getLayoutParams ().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        //Log.d("debug","onBindViewHolder");
        MovieImages.Poster output =  moviePosters.get(position);
        String url = "https://image.tmdb.org/t/p/w780/" + output.getFilePath();
        //Log.d("debug","url = " + url);
        // load image
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .into(holder.riv);
        //holder.riv.setImageBitmap(bitmapList.get(position));
    }


    @Override
    public int getItemCount()
    {
        //Log.d("debug", "length = " + String.valueOf(moviePosters.size()));
        return moviePosters.size();
    }
}