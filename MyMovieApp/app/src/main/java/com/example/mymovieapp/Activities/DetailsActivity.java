package com.example.mymovieapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mymovieapp.Fragments.FavouriteFragment;
import com.example.mymovieapp.Helpers.BooVariable;
import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterFavouriteMovies;
import com.example.mymovieapp.Helpers.RecyclerViewAdapters.HorizontalMoviesAdapter;
import com.example.mymovieapp.Helpers.RecyclerViewAdapters.HorizontalPhotosAdapter;
import com.example.mymovieapp.Helpers.SaveMovieToDB;
import com.example.mymovieapp.Helpers.RecyclerItemClickListener;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieDetails;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieImages;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieRecomendations;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.SearchByMovieId;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.SearchByMovieTitle;
import com.example.mymovieapp.Helpers.YouTubeConfig;
import com.example.mymovieapp.Interfaces.ApiInterface;
import com.example.mymovieapp.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailsActivity extends YouTubeBaseActivity
{

    List<SearchByMovieTitle.Result> listOfMoviesByTitle  = new ArrayList<>();
    List<SearchByMovieId.Result> listOfMoviesById = new ArrayList<>();
    List<MovieImages.Poster> moviePosters = new ArrayList<>();
    List<MovieImages.Backdrop> movieBackdrops = new ArrayList<>();
    List<MovieRecomendations.Result> movieRecommendations = new ArrayList<>();


    RecyclerView horizontal_recycler_view;
    HorizontalPhotosAdapter horizontalAdapter;

    RecyclerView horizontal_recycler_view2;
    HorizontalMoviesAdapter horizontalAdapter2;


    public static String BASE_URL = "https://api.themoviedb.org";
    public static  int PAGE = 1;
    public static String API_KEY = "78cf14f404a2a390e49c26846d6ec63d";
    public static String LANGUAGE ="en-US";
    public static  String CATEGORY = "popular";

    public static  String MOVIE = "movie";
    public static  String ADULT = "false";

    String movie_title = new String();
    int movie_id;
    String movie_path = new String();
    String movie_description = new String();

    volatile BooVariable bv = new BooVariable();

    YouTubePlayerView youTubePlayerView;
    Button btnPlay;
    Button likeButton;
    YouTubePlayer.OnInitializedListener onInitializedListener;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String title = getIntent().getStringExtra("MOVIE_TITLE");
        TextView tv = findViewById(R.id.textView2);
        tv.setText(title);

        Button backButton = (Button)this.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backButton.setBackgroundColor(0xfffff);





        //Like gomb
        likeButton = findViewById(R.id.like_button);
        likeButton.setEnabled(false);


        bv.setListener(new BooVariable.ChangeListener()
        {
            @Override
            public void onChange()
            {
                likeButton.setEnabled(true);

                likeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        SaveMovieToDB saveMovieToDB = SaveMovieToDB.getInstance();
                        saveMovieToDB.saveFavouriteMovieToDatabase(DetailsActivity.this, movie_title, movie_description, moviePosters);
                        DBAdapterFavouriteMovies db = new DBAdapterFavouriteMovies(getApplicationContext());
                        db.printEverything();
                        FavouriteFragment.notifyChange();
                    }
                });

                DBAdapterFavouriteMovies db = new DBAdapterFavouriteMovies(getApplicationContext());
                db.printEverything();
            }
        });



        //Toast.makeText(getApplicationContext(), title + " one", Toast.LENGTH_SHORT).show();

        //osztalyszintu film cim beallitasa
        this.movie_title = title;

        doApiCall(1, -1);

        //Log.d("debug","letrejott");
    }







    private void doApiCall(final int selector, final int id)
    {

        //Log.d("debug","selector = " + selector);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
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
                if(selector == 1)
                {
                    Call<SearchByMovieTitle> call = apiInterface.searchResultByTitle(MOVIE , API_KEY, LANGUAGE, movie_title, 1, ADULT);

                    call.enqueue(new Callback<SearchByMovieTitle>()
                    {
                        @Override
                        public void onResponse(Call<SearchByMovieTitle> call, Response<SearchByMovieTitle> response)
                        {
                            //parse results
                            SearchByMovieTitle results = response.body();

                            //put them into the arraylist
                            listOfMoviesByTitle = results.getResults();

                            //set global variable
                            movie_id = listOfMoviesByTitle.get(0).getId();

                            //chaining number 1
                            doApiCall(2, movie_id);
                        }

                        @Override
                        public void onFailure(Call<SearchByMovieTitle> call, Throwable t)
                        {
                            //Log.d("debug","elakadas 1");
                            if(t.getMessage() != null)
                            {
                                //Log.d("debug",t.getMessage());
                            }

                            //Log.d("debug",t.getCause().toString());
                        }
                    });

                }
                //YOUTUBE KULCS LEKERESE, VIDEO LEKERESE
                else if(selector == 2)
                {

                    Call<SearchByMovieId> call = apiInterface.searchResultById(String.valueOf(id) ,API_KEY, LANGUAGE);
                    //Call<MovieResults> call = apiInterface.searchResult("movie" , API_KEY, LANGUAGE, newText,  1, "false");

                    call.enqueue(new Callback<SearchByMovieId>()
                    {
                        @Override
                        public void onResponse(Call<SearchByMovieId> call, Response<SearchByMovieId> response)
                        {
                            //parse results
                            SearchByMovieId results = response.body();
                            if(results != null)
                            {
                                listOfMoviesById = results.getResults();

                                movie_path = listOfMoviesById.get(0).getKey();

                                iniVideoPlayer(movie_path);
                            }
                            else
                            {
                                //Log.d("debug","problem!");
                            }

                            //chaining number 2
                            //Log.d("debug", "ok 2");
                            doApiCall(3, movie_id);
                        }

                        @Override
                        public void onFailure(Call<SearchByMovieId> call, Throwable t)
                        {
                            //Log.d("debug","elakadas 2");
                            if(t.getMessage() != null)
                            {
                                //Log.d("debug",t.getMessage());
                            }

                            //Log.d("debug",t.getCause().toString());
                        }
                    });
                }
                //KEPEK LEKERESE
                else if(selector == 3)
                {
                    //Log.d("debug","belepett a 3-ba");
                    Call<MovieImages> call = apiInterface.getImages(String.valueOf(id),API_KEY,"en");

                    call.enqueue(new Callback<MovieImages>()
                    {
                        @Override
                        public void onResponse(Call<MovieImages> call, Response<MovieImages> response)
                        {
                            //parse results
                            MovieImages results = response.body();

                            //put them into the arraylist
                            moviePosters = results.getPosters();
                            movieBackdrops = results.getBackdrops();

                            //Log.d("debug", "movie posters length = "+String.valueOf(moviePosters.size()));


                            //chaining number 3
                            //Log.d("debug", "ok 3");
                            doApiCall(4, movie_id);
                        }

                        @Override
                        public void onFailure(Call<MovieImages> call, Throwable t)
                        {
                            //Log.d("debug","elakadas 3");
                            if(t.getMessage() != null)
                            {
                                //Log.d("debug",t.getMessage());
                            }

                            //Log.d("debug",t.getCause().toString());
                        }
                    });
                }
                //LEIRAS LEKERESE
                else if(selector == 4)
                {
                    Call<MovieDetails> call = apiInterface.getDetails(String.valueOf(id),API_KEY,LANGUAGE);

                    call.enqueue(new Callback<MovieDetails>()
                    {
                        @Override
                        public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response)
                        {
                            //parse results
                            MovieDetails results = response.body();

                            TextView tv = findViewById(R.id.description);
                            tv.setText(results.getOverview());

                            movie_description = results.getOverview();

                            bv.setBoo(true);

                            //chaining number 3
                            //Log.d("debug", "ok 4");
                            doApiCall(5, movie_id);
                        }

                        @Override
                        public void onFailure(Call<MovieDetails> call, Throwable t)
                        {
                            //Log.d("debug","elakadas 4");
                            if(t.getMessage() != null)
                            {
                                //Log.d("debug",t.getMessage());
                            }

                            //Log.d("debug",t.getCause().toString());
                        }
                    });
                }
                //AJANLOTT FILMEK LEKERESE
                else if(selector == 5)
                {
                    Call<MovieRecomendations> call = apiInterface.getRecomendations(String.valueOf(id),API_KEY,LANGUAGE);

                    call.enqueue(new Callback<MovieRecomendations>()
                    {
                        @Override
                        public void onResponse(Call<MovieRecomendations> call, Response<MovieRecomendations> response)
                        {
                            //parse results
                            MovieRecomendations results = response.body();

                            //put them into the arraylist
                            movieRecommendations = results.getResults();

                            //chaining number 4
                            //Log.d("debug", "ok 5");

                            finishing_it();
                            //doApiCall(6,-1);
                        }

                        @Override
                        public void onFailure(Call<MovieRecomendations> call, Throwable t)
                        {
                            //Log.d("debug","elakadas 5");
                            if(t.getMessage() != null)
                            {
                                //Log.d("debug",t.getMessage());
                            }

                            //Log.d("debug",t.getCause().toString());
                        }
                    });
                }
//                //innen kell kitorolni
//                else if(selector == 6)
//                {
//                    Call<MoviesNowPlaying> call = apiInterface.nowPlaying(API_KEY, LANGUAGE);
//
//                    call.enqueue(new Callback<MoviesNowPlaying>()
//                    {
//                        @Override
//                        public void onResponse(Call<MoviesNowPlaying> call, Response<MoviesNowPlaying> response)
//                        {
//                            Log.d("database3","got response!");
//                            //parse
//                            MoviesNowPlaying results = response.body();
//
//
//                            ArrayList<MoviesNowPlaying.Result> nowPlayingMovies;
//                            //put them into the arraylist
//                            nowPlayingMovies = (ArrayList<MoviesNowPlaying.Result>) results.getResults();
//
//                            //TODO
//                            //kell egy szurest csinalni, a megfelelo datumu filmek megtartasaval
//
//                            finishing_it();
//
//                            //bv.setBoo(true);
//                        }
//
//                        @Override
//                        public void onFailure(Call<MoviesNowPlaying> call, Throwable t)
//                        {
//                            Log.d("database3","elakadas 1");
//                            if(t.getMessage() != null)
//                            {
//                                Log.d("database3", "DEBUGGING" + t.getMessage());
//                            }
//
//                            //Log.d("database3",t.getCause().toString());
//                        }
//                    });
//                }
            }
        }, 0);
    }




    void iniVideoPlayer(final String path)
    {

        btnPlay = findViewById(R.id.youtube_view_button);
        youTubePlayerView = findViewById(R.id.youtube_view);


        onInitializedListener = new YouTubePlayer.OnInitializedListener()
        {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                //Log.d("debug", "done initializing");

                youTubePlayer.loadVideo(path);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                //youTubePlayer.seekToMillis(2000);

                //youTubePlayerView.initialize(YouTubeConfig.getApiKey(), onInitializedListener);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
            {
               // Log.d("debug", "failed initializing");
            }
        };

        btnPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                youTubePlayerView.initialize(YouTubeConfig.getApiKey(), onInitializedListener);
            }
        });
    }


    void finishing_it()
    {
        //kepek
        horizontalAdapter = new HorizontalPhotosAdapter(getApplicationContext(), moviePosters);
        horizontal_recycler_view = findViewById(R.id.horizontal_recycler_view);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontalAdapter.notifyDataSetChanged();
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager( getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


        //ajanlott filmek
        horizontalAdapter2 = new HorizontalMoviesAdapter(getApplicationContext(), movieRecommendations);
        horizontal_recycler_view2 = findViewById(R.id.horizontal_recycler_view2);
        horizontal_recycler_view2.setAdapter(horizontalAdapter2);
        horizontalAdapter2.notifyDataSetChanged();
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager( getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view2.setLayoutManager(horizontalLayoutManager2);
        horizontal_recycler_view2.setAdapter(horizontalAdapter2);



        horizontal_recycler_view2.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), horizontal_recycler_view2 ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position)
                    {
                        //Log.d("debug","fragment csere");
                        //Toast.makeText(getApplicationContext(),"you have clicked on a recomended movie " + position, Toast.LENGTH_SHORT).show();
                        //DetailsFragment detailFragment = new DetailsFragment();
                        //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        //TextView myview = view.findViewById(R.id.tv_title);
                        //Log.d("debug", myview.getText().toString() + "home");

                        //fragmens-el v1
                        //fragmentTransaction.replace(R.id.fg_placeholder, DetailsFragment.newInstance(myview.getText().toString() ));
                        //fragmentTransaction.replace(R.id.fg_placeholder, DetailsFragment.newInstance(listOfMovies.get(position))); //nem jo
                        //fragmentTransaction.addToBackStack("back");
                        //fragmentTransaction.commit();

                        //fragmens-el v2
                        //PlayerYouTubeFrag myFragment = PlayerYouTubeFrag.newInstance("video_id");
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video_container, (android.support.v4.app.Fragment) myFragment).commit();


                        TextView myview = view.findViewById(R.id.movie_title);


                        finish();
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
                        intent.putExtra("MOVIE_TITLE", myview.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(0, 0);


                        //activity-vel
//                        finish();
//                        Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
//                        intent.putExtra("MOVIE_TITLE", myview.getText().toString());
//                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position)
                    {
                        //Toast.makeText(getApplicationContext(),"you have long clicked on element " + position, Toast.LENGTH_SHORT).show();
                    }
                }));

    }
}
