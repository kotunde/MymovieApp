package com.example.mymovieapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymovieapp.Activities.DetailsActivity;
import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterFavouriteMovies;
import com.example.mymovieapp.Helpers.RecyclerViewAdapters.FavouriteMoviesAdapter;
import com.example.mymovieapp.Helpers.RecyclerItemClickListener;
import com.example.mymovieapp.Models.MovieModels.FavouriteMovie;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieResults;
import com.example.mymovieapp.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment
{


    public static String BASE_URL = "https://api.themoviedb.org";
    public static  int PAGE = 1;
    public static String API_KEY = "78cf14f404a2a390e49c26846d6ec63d";
    public static String LANGUAGE ="en-US";
    public static  String CATEGORY = "popular";

    public static  String MOVIE = "movie";
    public static  String ADULT = "false";

    RecyclerView mRecyclerView;

    static  FavouriteMoviesAdapter mMoviesAdapter;

    List<MovieResults.Result> listOfMovies;
    List<MovieResults.Result> resultMovies;

    //-----------------------------------------------------------------------------------------------


    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    private String mUsername;
    private String mPassword;


    public static  void notifyChange()
    {
        mMoviesAdapter.notifyDataSetChanged();
    }


    public FavouriteFragment()
    {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance(String username, String password)
    {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //kereses
        setHasOptionsMenu(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_favourite, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favourites");
        initView(retView);


        //adatbazistol lekerdezes
        DBAdapterFavouriteMovies db = new DBAdapterFavouriteMovies(getContext());
        //Log.d("database", "fragmensbol");
        db.printEverything();

        ArrayList<FavouriteMovie> result = db.getFavouriteMovies();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new FavouriteMoviesAdapter(getContext(), result);
        mRecyclerView.setAdapter(mMoviesAdapter);

        return retView;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        //Log.d("database", "fragmensbol");
        DBAdapterFavouriteMovies db = new DBAdapterFavouriteMovies(getContext());
        db.printEverything();
        ArrayList<FavouriteMovie> result = db.getFavouriteMovies();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new FavouriteMoviesAdapter(getContext(), result);
        mRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.search_bar, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
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
                    return false;
                }
                else
                {
                    resultMovies.clear();
                    return false;
                }
            }
        });
    }

    public void initView(View view)
    {
        //setup RecyclerView
        mRecyclerView = view.findViewById(R.id.rv_movieList_favourites);
        mRecyclerView.addOnItemTouchListener
        (
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override public void onItemClick(View view, int position)
                    {
                        //Log.d("debug","fragment csere");
                        //Toast.makeText(getContext(),"you have clicked on element " + position, Toast.LENGTH_SHORT).show();
//                        DetailsFragment detailFragment = new DetailsFragment();
//                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                        TextView myview = view.findViewById(R.id.tv_title);

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new FavouriteMoviesAdapter(getContext(),new ArrayList<FavouriteMovie>());
        mRecyclerView.setAdapter(mMoviesAdapter);
    }
}
