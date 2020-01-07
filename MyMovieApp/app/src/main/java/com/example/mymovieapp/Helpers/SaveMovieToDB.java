package com.example.mymovieapp.Helpers;

import android.content.Context;

import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterFavouriteMovies;
import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterNowPlayingMovies;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieImages;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MoviesNowPlaying;

import java.util.ArrayList;
import java.util.List;

public class SaveMovieToDB
{
    private static final SaveMovieToDB ourInstance = new SaveMovieToDB();

    public static SaveMovieToDB getInstance()
    {
        return ourInstance;
    }

    DBAdapterFavouriteMovies db;
    DBAdapterNowPlayingMovies db2;


    private SaveMovieToDB()
    {

    }


    public boolean saveFavouriteMovieToDatabase(Context context, String title, String description, List<MovieImages.Poster> moviePosters)
    {
        db = new DBAdapterFavouriteMovies(context);

        db.getWritableDatabase();

        if(db.insertMovie(title, description, moviePosters, context))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public void saveNowPlayingMoviesToDatabase(Context context, ArrayList<MoviesNowPlaying.Result> nowPlayingMovies, int i)
    {
        db2 = new DBAdapterNowPlayingMovies(context, nowPlayingMovies);

        db2.getWritableDatabase();

        db2.insertMovies(context, nowPlayingMovies, i);
    }
}
