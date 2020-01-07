//Retrofit with tmdb
//https://www.youtube.com/watch?v=F9e7qvubo8Y
//Pagination
//https://androidwave.com/pagination-in-recyclerview/

package com.example.mymovieapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymovieapp.Activities.DetailsActivity;
import com.example.mymovieapp.Helpers.RecyclerViewAdapters.MoviesAdapter;
import com.example.mymovieapp.Helpers.PaginationListener;
import com.example.mymovieapp.Helpers.RecyclerItemClickListener;
import com.example.mymovieapp.Interfaces.ApiInterface;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieResults;
import com.example.mymovieapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mymovieapp.Helpers.PaginationListener.PAGE_START;


public class HomeFragment extends Fragment //implements SwipeRefreshLayout.OnRefreshListener
{
    public static String BASE_URL = "https://api.themoviedb.org";
    public static  int PAGE = 1;
    public static String API_KEY = "78cf14f404a2a390e49c26846d6ec63d";
    public static String LANGUAGE ="en-US";
    public static  String CATEGORY = "popular";

    public static  String MOVIE = "movie";
    public static  String ADULT = "false";

    RecyclerView mRecyclerView;
    MoviesAdapter mMoviesAdapter;
    List<MovieResults.Result> listOfMovies;
    List<MovieResults.Result> resultMovies;
    //private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


    public HomeFragment()
    {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }

        //makes the options appear in Toolbar
        setHasOptionsMenu(true); //???
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        initView(retView);
        return retView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.search_bar, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        //search(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchView.clearFocus();
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(newText.length() == 0)
                {
                    //Log.d("debug","ures uzenet");
                    mMoviesAdapter.clear();
                    doApiCall(1, "dummy");
                    return false;
                }
                else
                {
                    resultMovies.clear();
                    doApiCall(2, newText);
                    return false;
                }
            }
        });
    }

    public void initView(View view)
    {
        //setup RecyclerView
        mRecyclerView = view.findViewById(R.id.rv_movieList);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position)
                    {
                        //Log.d("debug","fragment csere");
                        //Toast.makeText(getContext(),"you have clicked on element " + position, Toast.LENGTH_SHORT).show();
                        DetailsFragment detailFragment = new DetailsFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        TextView myview = view.findViewById(R.id.tv_title);
                        //Log.d("debug", myview.getText().toString() + "home");

                        //fragmens-el v1
                        //fragmentTransaction.replace(R.id.fg_placeholder, DetailsFragment.newInstance(myview.getText().toString() ));
                        //fragmentTransaction.replace(R.id.fg_placeholder, DetailsFragment.newInstance(listOfMovies.get(position))); //nem jo
                        //fragmentTransaction.addToBackStack("back");
                        //fragmentTransaction.commit();

                        //fragmens-el v2
                        //PlayerYouTubeFrag myFragment = PlayerYouTubeFrag.newInstance("video_id");
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video_container, (android.support.v4.app.Fragment) myFragment).commit();

                        //activity-vel
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("MOVIE_TITLE", myview.getText().toString());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position)
                    {
                        //Toast.makeText(getContext(),"you have long clicked on element " + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );

        //swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(getContext(),new ArrayList<MovieResults.Result>());
        mRecyclerView.setAdapter(mMoviesAdapter);
        doApiCall(1, "dummy");


        //add scroll listener while user reach in bottom load more will call
        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager)
        {
            @Override
            protected void loadMoreItems()
            {
                isLoading = true;
                currentPage++;
                doApiCall(1, "dummy");
            }
            @Override
            public boolean isLastPage()
            {
                return isLastPage;
            }
            @Override
            public boolean isLoading()
            {
                return isLoading;
            }
        });

    }

    private void doApiCall(final int selector, final String newText)
    {
        listOfMovies = new ArrayList<>();
        resultMovies = new ArrayList<>();

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
                if(selector == 1)
                {
                    Call<MovieResults> call = apiInterface.listOfMovies(CATEGORY, API_KEY, LANGUAGE, currentPage);

                    call.enqueue(new Callback<MovieResults>()
                    {
                        @Override
                        public void onResponse(Call<MovieResults> call, Response<MovieResults> response)
                        {
                            //parse results
                            MovieResults results = response.body();
                            listOfMovies = results.getResults();
                            //mMoviesAdapter.addItems(listOfMovies);
                            itemCount = listOfMovies.size();
                            //mMoviesAdapter.notifyDataSetChanged(); //already called in the addItems method
                            //mRecyclerView.setAdapter(mMoviesAdapter); // Ha nem frissiti a recycler view-ot

                            //manage progress view
                            if (currentPage != PAGE_START) mMoviesAdapter.removeLoading();
                            mMoviesAdapter.addItems(listOfMovies);
                            //swipeRefreshLayout.setRefreshing(false);

                            // check weather is last page or not
                            if (currentPage < totalPage)
                            {
                                mMoviesAdapter.addLoading();
                            } else
                            {
                                isLastPage = true;
                            }
                            isLoading = false;
                        }

                        @Override
                        public void onFailure(Call<MovieResults> call, Throwable t)
                        {
                            t.printStackTrace();
                        }
                    });

                }
                else if(selector == 2)
                {
                    Call<MovieResults> call = apiInterface.searchResult(MOVIE , API_KEY, LANGUAGE, newText, currentPage, ADULT);
                    //Call<MovieResults> call = apiInterface.searchResult("movie" , API_KEY, LANGUAGE, newText,  1, "false");

                    call.enqueue(new Callback<MovieResults>()
                    {
                        @Override
                        public void onResponse(Call<MovieResults> call, Response<MovieResults> response)
                        {
                            mMoviesAdapter.clear();
                            //parse results
                            MovieResults results = response.body();
                            if(results != null)
                            {
                                resultMovies = results.getResults();
                            }

                            //mMoviesAdapter.addItems(resultMovies);
                            itemCount = resultMovies.size();
                            //mMoviesAdapter.notifyDataSetChanged(); //already called in the addItems method
                            //mRecyclerView.setAdapter(mMoviesAdapter); // Ha nem frissiti a recycler view-ot

                            //manage progress view
                            if (currentPage != PAGE_START) mMoviesAdapter.removeLoading();
                            mMoviesAdapter.addItems(resultMovies);
                            //swipeRefreshLayout.setRefreshing(false);

                            // check weather is last page or not
                            if (currentPage < totalPage)
                            {
                                mMoviesAdapter.addLoading();
                            } else
                            {
                                isLastPage = true;
                            }
                            isLoading = false;
                        }

                        @Override
                        public void onFailure(Call<MovieResults> call, Throwable t)
                        {
                            t.printStackTrace();
                        }
                    });
                }




            }
        }, 0);
    }

//    @Override
//    public void onRefresh()
//    {
//        itemCount = 0;
//        currentPage = PAGE_START;
//        mMoviesAdapter.clear();
//        doApiCall(1, "dummy");
//
//
//        //TODO refresh icon does not disappear
//        //swipeRefreshLayout.setRefreshing(false);
//        /*final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run()
//            {
//                if(swipeRefreshLayout.isRefreshing())
//                {
//                    swipeRefreshLayout.setRefreshing(false);
//                }
//            }
//        }, 1000);*/
//
//    }

}
