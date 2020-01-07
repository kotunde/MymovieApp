package com.example.mymovieapp.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.util.Log;

import com.example.mymovieapp.Helpers.BooVariable;
import com.example.mymovieapp.Helpers.SaveMovieToDB;
import com.example.mymovieapp.Interfaces.ApiInterface;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MoviesNowPlaying;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateDatabaseJobService extends JobService
{
    private static final String TAG = "UpdateDatabaseJobService";
    private boolean jobCancelled = false;


    public static String BASE_URL = "https://api.themoviedb.org";
    public static  int PAGE = 1;
    public static String API_KEY = "78cf14f404a2a390e49c26846d6ec63d";
    public static String LANGUAGE ="en-US";

    ArrayList<MoviesNowPlaying.Result> nowPlayingMovies;

    public UpdateDatabaseJobService()
    {
        super();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        //Log.d("database3", "Job Started");

        doBackgroundWork(jobParameters);

        return true;
    }

    public void doBackgroundWork(final JobParameters params)
    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                for(int i = 0; i < 10; ++i)
//                {
//                    Log.d("debug","run " + i);
//
//                    if(jobCancelled)
//                    {
//                        return;
//                    }
//
//
//                    try
//                    {
//                        Thread.sleep(1000);
//                    }
//                    catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }





                final BooVariable bv = new BooVariable();

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //Log.d("database3", "started running in background thread");
                        //retrofit with TMDB

                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        //instaciate interface
                        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                        //create a call, which executes asynchronously
                        //FILM ID LEKERESE

                        Call<MoviesNowPlaying> call = apiInterface.nowPlaying(API_KEY, LANGUAGE);

                        call.enqueue(new Callback<MoviesNowPlaying>()
                        {
                            @Override
                            public void onResponse(Call<MoviesNowPlaying> call, Response<MoviesNowPlaying> response)
                            {
                                //Log.d("database3","got response!");
                                //parse
                                MoviesNowPlaying results = response.body();

                                //put them into the arraylist
                                nowPlayingMovies = (ArrayList<MoviesNowPlaying.Result>) results.getResults();

                                //TODO
                                //kell egy szurest csinalni, a megfelelo datumu filmek megtartasaval

                                bv.setBoo(true);
                            }

                            @Override
                            public void onFailure(Call<MoviesNowPlaying> call, Throwable t)
                            {
                                //Log.d("database3","elakadas 1");
                                if(t.getMessage() != null)
                                {
                                    //Log.d("database3",t.getMessage());
                                }

                                //Log.d("database3",t.getCause().toString());
                            }
                        });
                    }
                }, 0);



                bv.setListener(new BooVariable.ChangeListener()
                {
                    @Override
                    public void onChange()
                    {
                        //Log.d("database3", "tryin to update the database");
                        //adatbazis frissitese
                        SaveMovieToDB saveMovieToDB = SaveMovieToDB.getInstance();
                        saveMovieToDB.saveNowPlayingMoviesToDatabase(getApplicationContext(),nowPlayingMovies,0);
                    }
                });


                //Log.d("database3","Job finished!");

                jobFinished(params, false);
            }
        //}).start();


    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        //Log.d("database3","Job cancelled before completion!");
        jobCancelled = true;
        return true;
    }
}
