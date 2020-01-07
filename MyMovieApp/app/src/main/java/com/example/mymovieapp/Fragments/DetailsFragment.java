package com.example.mymovieapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mymovieapp.R;
import com.example.mymovieapp.Helpers.YouTubeConfig;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class DetailsFragment extends Fragment//YouTubePlayerSupportFragment
{

    YouTubePlayerView youTubePlayerView;
    Button btnPlay;
    YouTubePlayer.OnInitializedListener onInitializedListener;


    private YouTubePlayer mYoutubePlayer;
    private String mVideoId;



    public void disableButton()
    {

    }

    public void enableButton()
    {

    }

    private static final String ARG_TITLE = "title";
    VideoView myvideo;
    String title = new String();


    public static DetailsFragment newInstance(String title)
    {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        //Log.d("debug",title + "details");
        return fragment;
    }

    public DetailsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();

        if (arguments == null || !arguments.containsKey(ARG_TITLE)) {
            // Set a default or error as you see fit
        } else {
            title = arguments.getString(ARG_TITLE);
        }

        //Log.d("debug",title + "valami");
        //Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
        //title = savedInstanceState.getString(ARG_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //myvideo= view.findViewById(R.id.videoView);
        //myvideo.setMediaController(new MediaController(getContext()));
        //Uri video = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.small);
        //myvideo.setVideoURI(video);
        //myvideo.setVideoPath("https://www.youtube.com/watch?v=adzYW5DZoWs");
        //myvideo.seekTo(1);
        //Toast.makeText(getContext(),this.title, Toast.LENGTH_SHORT).show();
        //myvideo.start();
        //myvideo.

        btnPlay = view.findViewById(R.id.youtube_view_button);
        youTubePlayerView = view.findViewById(R.id.youtube_view);

        onInitializedListener = new YouTubePlayer.OnInitializedListener()
        {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                //Log.d("debug", "done initializing");

                youTubePlayer.loadVideo("W4hTJybfU7s");

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
            {
                //Log.d("debug", "failed initializing");
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


//        Intent intent = new Intent(getActivity(), DetailsActivity.class);
//        startActivity(intent);


    }



//    private void initializeYoutubeFragment()
//    {
//
//        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//
//        youTubePlayerFragment.initialize("W4hTJybfU7s", new YouTubePlayer.OnInitializedListener()
//        {
//
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
//            {
//                if (!wasRestored) {
//                    mYoutubePlayer = player;
//                    mYoutubePlayer.setShowFullscreenButton(false);
//                    mYoutubePlayer.cueVideo(mVideoId);
//                }
//
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1)
//            {
//
//            }
//        });
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.fl_youtube, youTubePlayerFragment ).commit();
//        transaction.add(R.id.fl_youtube, youTubePlayerFragment)
//    }




    @Override
    public void onResume()
    {
        super.onResume();
    }




}
