package com.example.mymovieapp.Models.MovieModels;

import android.graphics.Bitmap;

import java.util.Date;

public class NowPlayingMovie
{
    private String title;
    private String description;
    private byte[] image;
    private Date date;

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

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
