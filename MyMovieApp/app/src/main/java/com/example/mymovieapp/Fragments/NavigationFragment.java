//Bottom Navigation
//https://tutorialwing.com/android-bottom-navigation-view-tutorial-with-example/
//Icons
//https://icons8.com/icons/pack/cinema

package com.example.mymovieapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterNowPlayingMovies;
import com.example.mymovieapp.Models.MovieModels.NowPlayingMovie;
import com.example.mymovieapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationFragment extends Fragment
{
    final Fragment homeFragment = new HomeFragment();
    final Fragment profileFragment = new ProfileFragment();
    final Fragment favouriteFragment = new FavouriteFragment();
    final Fragment cinemaFragment = new CinemaFragment();
    Fragment active = homeFragment;
    FragmentManager fragmentManager;

    //final Fragment
    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    private String mUsername;
    private String mPassword;

    public NavigationFragment()
    {
        // Required empty public constructor
    }

    public static NavigationFragment newInstance(String username, String password)
    {
        NavigationFragment fragment = new NavigationFragment();
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
        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USERNAME);
            mPassword = getArguments().getString(ARG_PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View retView =  inflater.inflate(R.layout.fragment_navigation, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("username", mUsername);
        bundle.putString("password", mPassword);
        profileFragment.setArguments(bundle);

        fragmentManager.beginTransaction().add(R.id.fg_navPlaceholder,profileFragment,"profile").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fg_navPlaceholder,favouriteFragment,"favourite").hide(favouriteFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fg_navPlaceholder,cinemaFragment,"cinema").hide(cinemaFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fg_navPlaceholder,homeFragment,"home").commit();
        initView(retView);
        return retView;
    }
    public void initView(View view)
    {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_home:
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");

                        //add icon to the action bar
                        /*ImageView imageView = new ImageView(getActivity().getApplicationContext());
                        imageView.setImageResource(R.drawable.tmdb);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(imageView,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        //enable the view
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);*/

                        fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        //Toast.makeText(getActivity(),"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_profile:
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");
                        fragmentManager.beginTransaction().hide(active).show(profileFragment).commit();
                        active = profileFragment;
                        //Toast.makeText(getActivity(),"Profile",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_favourites:
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favourites");
                        fragmentManager.beginTransaction().hide(active).show(favouriteFragment).commit();
                        active = favouriteFragment;
                        //Toast.makeText(getActivity(),"Favourites",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_cinema:
                        if(DBAdapterNowPlayingMovies.bv.isBoo())
                        {
                            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Now in cinema");
                            fragmentManager.beginTransaction().hide(active).show(cinemaFragment).commit();
                            active = cinemaFragment;
                            cinemaFragment.onResume();
                            //Toast.makeText(getActivity(),"Now in cinema",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Syncing, please Wait!", Toast.LENGTH_LONG).show();
                        }

                        break;
                }
                return true;
            }
        });
    }
}
