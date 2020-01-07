package com.example.mymovieapp.Helpers.RecyclerViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieRecomendations;
import com.example.mymovieapp.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;


public class HorizontalMoviesAdapter extends RecyclerView.Adapter<HorizontalMoviesAdapter.MyViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MovieRecomendations.Result> movieRecomendations;


    public HorizontalMoviesAdapter(Context context, List<MovieRecomendations.Result> temp_movie_recomendations)
    {
        //Log.d("debug","HorizontalMoviesAdapter");
        this.movieRecomendations = (ArrayList<MovieRecomendations.Result>) temp_movie_recomendations;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView riv;
        private TextView title;

        public MyViewHolder(View view)
        {
            super(view);

            riv = view.findViewById(R.id.horizontal_item_view_image);
            title = view.findViewById(R.id.movie_title);
        }
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //Log.d("debug","onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_movie_view, parent, false);

        if (itemView.getLayoutParams ().width == RecyclerView.LayoutParams.MATCH_PARENT)
            itemView.getLayoutParams ().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        return new HorizontalMoviesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HorizontalMoviesAdapter.MyViewHolder holder, final int position)
    {
        //Log.d("debug","onBindViewHolder");
        MovieRecomendations.Result output =  movieRecomendations.get(position);

        String url = "https://image.tmdb.org/t/p/w780/" + output.getPosterPath();
        //Log.d("debug","url = " + url);
        // load image
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .into(holder.riv);

        holder.title.setText(movieRecomendations.get(position).getTitle());
        //holder.mov.setText("movie title");
        //holder.riv.setImageBitmap(bitmapList.get(position));
    }


    @Override
    public int getItemCount()
    {
        //Log.d("debug", "length = " + String.valueOf(movieRecomendations.size()));
        return movieRecomendations.size();
    }
}


