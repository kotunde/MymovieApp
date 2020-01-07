package com.example.mymovieapp.Models.MovieModels;

import android.graphics.Bitmap;

public class FavouriteMovie
{
    private String title;
    private String description;
    private byte[] image;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }
}
