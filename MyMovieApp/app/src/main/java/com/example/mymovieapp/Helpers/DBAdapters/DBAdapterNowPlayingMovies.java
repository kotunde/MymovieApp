package com.example.mymovieapp.Helpers.DBAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymovieapp.Helpers.BooVariable;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MoviesNowPlaying;
import com.example.mymovieapp.Models.MovieModels.NowPlayingMovie;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBAdapterNowPlayingMovies extends SQLiteOpenHelper
{
    public static BooVariable bv = new BooVariable();

    public static String BASE_URL = "https://image.tmdb.org/t/p/w200/";

    public static final String DATABASE_NAME = "MovieAppNowPlayingMovies.db";
    int i;

    SQLiteDatabase db;
    Bitmap image ;
    ContentValues contentValues;

    public DBAdapterNowPlayingMovies(Context context, ArrayList<MoviesNowPlaying.Result> nowPlayingMovies)
    {
        super(context, DATABASE_NAME,null,1);
        Log.d("database", "constructor");

        if(!isTableExists("NowPlayingMovie", true))
        {
            db = getWritableDatabase();
            db.execSQL("CREATE TABLE  NowPlayingMovie (id integer primary key, title VARCHAR, description VARCHAR, image BLOB, date DATE)");
            Log.d("database","create table lefutott");
            db.close();
        }
    }



    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("database", "on create");
        //db.execSQL("CREATE TABLE  NowPlayingMovie (id integer primary key, title VARCHAR, description VARCHAR, image BLOB)");
    }



    @Override
    public synchronized void close()
    {
        super.close();
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("database", "on upgrade");
        db.execSQL("DROP TABLE IF EXISTS NowPlayingMovie");
        onCreate(db);
    }



    public boolean isTableExists(String tableName, boolean openDb)
    {
        if(openDb) {
            if(db == null || !db.isOpen())
            {
                db = getReadableDatabase();
            }

            if(!db.isReadOnly())
            {
                db.close();
                db = getReadableDatabase();
            }
        }

        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'";
        try (Cursor cursor = db.rawQuery(query, null))
        {
            if(cursor!=null)
            {
                if(cursor.getCount()>0)
                {
                    return true;
                }
            }
            return false;
        }
        finally
        {
            db.close();
        }
    }



    public void  printEverything()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM NowPlayingMovie",null);

        Log.d("database", " ");
        Log.d("database", " ");
        Log.d("database", " ");
        while(res.moveToNext())
        {
            String result = res.getString(res.getColumnIndex("title")) + " -> " +res.getString(res.getColumnIndex("description"));
            Log.d("database",result);
        }
        Log.d("database", "  ");
        Log.d("database", " ");
        Log.d("database", " ");

        db.close();
    }



    public ArrayList<NowPlayingMovie> getNowPlayingMovies()
    {
        ArrayList<NowPlayingMovie> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM NowPlayingMovie",null);


        while(res.moveToNext())
        {
            NowPlayingMovie nowPlayingMovie = new NowPlayingMovie();

            Log.d("title", res.getString(res.getColumnIndex("title")));
            nowPlayingMovie.setTitle(res.getString(res.getColumnIndex("title")));
            nowPlayingMovie.setDescription(res.getString(res.getColumnIndex("description")));
            nowPlayingMovie.setImage(res.getBlob(res.getColumnIndex("image")));

            Log.d("now","resutl sent");
            result.add(nowPlayingMovie);
        }

        res.close();
        return result;
    }


    public void insertMovies(final Context context, final ArrayList<MoviesNowPlaying.Result> movies, final int i)
    {

        db = getWritableDatabase();

        if(i == 0)
        {
            Log.d("database3","dropping the table");
            db.execSQL("DROP TABLE IF EXISTS NowPlayingMovie");
            db.execSQL("CREATE TABLE  NowPlayingMovie (id integer primary key, title VARCHAR, description VARCHAR, image BLOB, date DATE)");
        }



        int count = movies.size();

        Log.d("rec",String.valueOf(i));

        if(i >= count)
        {
            bv.setBoo(true);
            return;
        }

        if (!CheckIsDataAlreadyInDBorNot(movies.get(i).getTitle()))
        {
            Log.d("database3", "inserting this: " + movies.get(i).getTitle());

            //Log.d("database2", "title " + title);
            //Log.d("database2", "description " + description);

            contentValues = new ContentValues();
            contentValues.put("title", "'" + movies.get(i).getTitle().replaceAll("'","") + "'");
            contentValues.put("description", "'" + movies.get(i).getOverview() +"'");
            contentValues.put("date", "'" + movies.get(i).getReleaseDate() + "'");

            //Log.d("title",  movies.get(i).getTitle().replaceAll("'","") + "'");

            //kep letoltese
            String path = BASE_URL + movies.get(i).getPosterPath();



            Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .into(new CustomTarget<Bitmap>()
                    {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition)
                        {
                            Log.d("debug5","megerkezett");
                            image = resource;
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                            byte[] byteArray = outputStream.toByteArray();


                            contentValues.put("image", byteArray);

                            try
                            {
                                db = getWritableDatabase();
                                if(db.insert("NowPlayingMovie", null, contentValues) > 0)
                                {
                                    Log.d("database","siker");
                                    insertMovies( context, movies, i+1);
                                }
                                else
                                {
                                    Log.d("database","bukas");
                                }
                                db.close();
                            }
                            catch (Exception e)
                            {
                                e.toString();
                                Log.d("debug5",e.toString());
                            }

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder)
                        {
                        }
                    });
            }
    }




    private boolean CheckIsDataAlreadyInDBorNot(String title)
    {
        db = this.getReadableDatabase();

        int count = 0;

        Cursor cursor = db.rawQuery("SELECT title FROM NowPlayingMovie WHERE title = ?;", new String[] {"'" + title + "'"});

        while(cursor.moveToNext())
        {
            count++;
        }

        Log.d("database", "count = " + String.valueOf(count));

        if(cursor.getCount() <= 0)
        {
            cursor.close();
            Log.d("database","returning false");
            return false;
        }

        cursor.close();
        Log.d("database","returning true");
        return true;
    }
}
