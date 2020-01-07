package com.example.mymovieapp.Fragments;

import android.os.Bundle;

import com.example.mymovieapp.Helpers.YouTubeConfig;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import androidx.fragment.app.Fragment;


public class PlayerYouTubeFrag extends YouTubePlayerFragment
{
    private String currentVideoID = "video_id";
    private YouTubePlayer activePlayer;

    public static PlayerYouTubeFrag newInstance(String url)
    {
        PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        //playerYouTubeFrag.setArguments(bundle);

        playerYouTubeFrag.init(); //This line right here

        return playerYouTubeFrag;
    }

    private void init()
    {
        initialize(YouTubeConfig.getApiKey(), new YouTubePlayer.OnInitializedListener()
        {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1)
            {
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
            {
                activePlayer = player;
                activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                if (!wasRestored)
                {
                    activePlayer.loadVideo("6dnfz_O9pxQ", 0);

                }
            }
        });
    }
}
