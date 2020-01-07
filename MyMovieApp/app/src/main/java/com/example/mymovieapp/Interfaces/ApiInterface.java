package com.example.mymovieapp.Interfaces;

import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieDetails;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieImages;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieRecomendations;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MoviesNowPlaying;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.SearchByMovieId;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.SearchByMovieTitle;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface
{
    @GET("/3/movie/{category}")
    Call<MovieResults> listOfMovies
    (
        @Path("category") String category,
        @Query("api_key") String apiKey,
        @Query("language") String language,
        @Query("page") int page
    );


    @GET("/3/search/{movie}")
    Call<MovieResults> searchResult
    (
            @Path("movie") String movie,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page,
            @Query("include_adult") String adult
    );



    @GET("/3/search/{movie}")
    Call<SearchByMovieTitle> searchResultByTitle
    (
            @Path("movie") String movie,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page,
            @Query("include_adult") String adult
    );

    @GET("/3/movie/{id}/videos")
    Call<SearchByMovieId> searchResultById
    (
            //@Path("movie") String movie,
            @Path("id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("/3/movie/{id}/images")
    Call<MovieImages> getImages
    (
            //@Path("movie") String movie,
            @Path("id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );


    @GET("/3/movie/{id}/recommendations")
    Call<MovieRecomendations> getRecomendations
    (
            //@Path("movie") String movie,
            @Path("id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("/3/movie/{id}")
    Call<MovieDetails> getDetails
    (
            //@Path("movie") String movie,
            @Path("id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("/3/movie/now_playing")
    Call<MoviesNowPlaying> nowPlaying
    (
            //@Path("movie") String movie,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

}
