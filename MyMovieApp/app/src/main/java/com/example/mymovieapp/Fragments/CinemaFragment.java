package com.example.mymovieapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymovieapp.Activities.DetailsActivity;
import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterFavouriteMovies;
import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterNowPlayingMovies;
import com.example.mymovieapp.Helpers.RecyclerItemClickListener;
import com.example.mymovieapp.Helpers.RecyclerViewAdapters.FavouriteMoviesAdapter;
import com.example.mymovieapp.Helpers.RecyclerViewAdapters.NowPlayingMoviesAdapter;
import com.example.mymovieapp.Models.MovieModels.FavouriteMovie;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MoviesNowPlaying;
import com.example.mymovieapp.Models.MovieModels.NowPlayingMovie;
import com.example.mymovieapp.R;

import java.util.ArrayList;

public class CinemaFragment extends Fragment
{
    RecyclerView mRecyclerView;
    NowPlayingMoviesAdapter mMoviesAdapter;
    ArrayList<NowPlayingMovie> nowPlayingMovies;

    //------------------------------------------------------------------------------------------

    public CinemaFragment()
    {
        // Required empty public constructor
    }

    public static CinemaFragment newInstance(String param1, String param2)
    {
        CinemaFragment fragment = new CinemaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Override
    public void onResume()
    {

        Log.d("rec","visszatert");
        super.onResume();

        DBAdapterNowPlayingMovies db = new DBAdapterNowPlayingMovies(getContext(), new ArrayList<MoviesNowPlaying.Result>());

        nowPlayingMovies = db.getNowPlayingMovies();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new NowPlayingMoviesAdapter(getContext(), nowPlayingMovies);
        mRecyclerView.setAdapter(mMoviesAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_cinema, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cinema");
        initView(retView);
        return retView;
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//    {
//        inflater.inflate(R.menu.search_bar, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        //search(searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//                searchView.clearFocus();
//                onQueryTextChange(query);
//                return false;
//            }
//
//
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                if(newText.length() == 0)
//                {
//                    Log.d("debug","ures uzenet");
//                    mMoviesAdapter.clear();
//                    loadMoviesFromDB();
//                    return false;
//                }
//                else
//                {
//                    resultMovies.clear();
//                    loadMoviesFromDB();
//                    return false;
//                }
//            }
//        });
//    }

    public void initView(View view)
    {
        //setup RecyclerView
        mRecyclerView = view.findViewById(R.id.rv_movieList_now_playing);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override public void onItemClick(View view, int position)
                    {
                        //Log.d("debug","fragment csere");
                        //Toast.makeText(getContext(),"you have clicked on element " + position, Toast.LENGTH_SHORT).show();
                        DetailsFragment detailFragment = new DetailsFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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

        //adatbazisbol lekerni az adatokat
        DBAdapterNowPlayingMovies db = new DBAdapterNowPlayingMovies(getContext(), new ArrayList<MoviesNowPlaying.Result>());

        nowPlayingMovies = db.getNowPlayingMovies();

        //mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new NowPlayingMoviesAdapter(getContext(), nowPlayingMovies);
        mRecyclerView.setAdapter(mMoviesAdapter);
        //loadMoviesFromDB();
    }
}
