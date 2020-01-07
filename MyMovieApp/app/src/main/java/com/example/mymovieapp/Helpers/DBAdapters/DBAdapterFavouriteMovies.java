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
import com.example.mymovieapp.Models.MovieModels.FavouriteMovie;
import com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase.MovieImages;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class DBAdapterFavouriteMovies extends SQLiteOpenHelper
{


    public static String BASE_URL = "https://image.tmdb.org/t/p/w200/";
    public static final String DATABASE_NAME = "MovieAppFavouriteMovies.db";


    SQLiteDatabase db;
    Bitmap image ;
    ContentValues contentValues;


    public DBAdapterFavouriteMovies(Context context)
    {
        super(context, DATABASE_NAME,null,1);
        //Log.d("database", "constructor");
        if(!isTableExists("FavouriteMovies", true))
        {
            db = getWritableDatabase();
            db.execSQL("CREATE TABLE  FavouriteMovies (id integer primary key, title VARCHAR, description VARCHAR, image BLOB)");
            //Log.d("database","create table lefutott");
            db.close();
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Log.d("database", "on create");
        //db.execSQL("CREATE TABLE  FavouriteMovies (id integer primary key, title VARCHAR, description VARCHAR, image BLOB)");
    }

    @Override
    public synchronized void close()
    {
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Log.d("database", "on upgrade");
        db.execSQL("DROP TABLE IF EXISTS FavouriteMovies");
        onCreate(db);
    }


    public boolean isTableExists(String tableName, boolean openDb) {
        if(openDb) {
            if(db == null || !db.isOpen()) {
                db = getReadableDatabase();
            }

            if(!db.isReadOnly()) {
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
        Cursor res = db.rawQuery("SELECT * FROM FavouriteMovies",null);

        //Log.d("database", " ");
        //Log.d("database", " ");
        //Log.d("database", " ");
        while(res.moveToNext())
        {
            String result = res.getString(res.getColumnIndex("title")) + " -> " +res.getString(res.getColumnIndex("description"));
            //Log.d("database",result);
        }
        //Log.d("database", "  ");
        //Log.d("database", " ");
        //Log.d("database", " ");

        db.close();
    }

    public ArrayList<FavouriteMovie> getFavouriteMovies()
    {
        ArrayList<FavouriteMovie> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM FavouriteMovies",null);


        while(res.moveToNext())
        {
            FavouriteMovie favouriteMovie = new FavouriteMovie();

            favouriteMovie.setTitle(res.getString(res.getColumnIndex("title")));
            favouriteMovie.setDescription(res.getString(res.getColumnIndex("description")));
            favouriteMovie.setImage(res.getBlob(res.getColumnIndex("image")));

            //Log.d("now","resutl sent");
            result.add(favouriteMovie);
        }


        return result;
    }


    public boolean insertMovie(String title, String description, List<MovieImages.Poster> moviePosters, Context context)
    {
        if (!CheckIsDataAlreadyInDBorNot(title))
        {

            //Log.d("database2", "title " + title);
            //Log.d("database2", "description " + description);

            //Log.d("database", "meg nincs meg");
            db = getWritableDatabase();
            //Log.d("database", "insterMovie");

            contentValues = new ContentValues();
            contentValues.put("title", "'" + title.replaceAll("'","") + "'");
            contentValues.put("description", "'" + description +"'");

            //kep letoltese
            String path = BASE_URL + moviePosters.get(0).getFilePath();



            Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .into(new CustomTarget<Bitmap>()
                    {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition)
                        {
                            //Log.d("debug5","megerkezett");
                            image = resource;
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                            byte[] byteArray = outputStream.toByteArray();


                            contentValues.put("image", byteArray);
                            if(db.insert("FavouriteMovies", null, contentValues) > 0)
                            {
                                //Log.d("database","siker");
                            }
                            else
                            {
                                //Log.d("database","bukas");
                            }
                            db.close();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder)
                        {
                        }
                    });


//            Bitmap theBitmap = Glide.
//                    with(context).
//                    asBitmap().
//                    load(path).
//                    into
//                        (
//                            new CustomTarget<Bitmap>()
//                            {
//                                @Override
//                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition)
//                                {
//                                    imageView.setImageBitmap(resource);
//                                }
//
//                                @Override
//                                public void onLoadCleared(@Nullable Drawable placeholder)
//                                {
//                                }
//                            }
//                        ). // Width and height
//                    get();


//            Bitmap image = null;
//            try
//            {
//                URL url = new URL(path);
//                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            }
//            catch(IOException e)
//            {
//                System.out.println(e);
//            }
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//            byte[] byteArray = outputStream.toByteArray();
//
//
//            contentValues.put("image", byteArray);
//            if(db.insert("FavouriteMovies", null, contentValues) > 0)
//            {
//                Log.d("database","siker");
//            }
//            else
//            {
//                Log.d("database","bukas");
//            }

            //db.close();
            return true;
        }

        return false;
    }

    private boolean CheckIsDataAlreadyInDBorNot(String title)
    {
        db = this.getReadableDatabase();

        int count = 0;

        Cursor cursor = db.rawQuery("SELECT title FROM FavouriteMovies WHERE title = ?;", new String[] {"'" + title + "'"});

        while(cursor.moveToNext())
        {
//            Log.d("database", ";lajdflksadjf");
//            String result = cursor.getString(cursor.getColumnIndex("title"));
//            Log.d("database", "masik" + result);
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


    public int getCount(String name)
    {
        Cursor c = null;
        try
        {
            db = getReadableDatabase();
            String query = "select count(*) from FavouriteMovies where title = ?";
            c = db.rawQuery(query, new String[] {name});

            if (c.moveToFirst())
            {
                return c.getInt(0);
            }

            return 0;
        }
        finally
        {
            if (c != null)
            {
                c.close();
            }

            if (db != null)
            {
                db.close();
            }
        }
    }


//    public boolean changePassword(String username, String password)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("password",password);
//
//        if(db.update("Users", contentValues, "username="+"'"+username+"'", null) > 0)
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }
//
//    public Cursor getDataUsers()
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM Users",null);
//        return res;
//    }
//
//    public Cursor getDataUsersById(int id)
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM Users where id ="+ id + "",null );
//        return res;
//    }
//
//    public Cursor getPasswordByUsername(String username)
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM Users where username=" + "'" + username + "'",null );
//        return res;
//    }


    //---------------------------------------------------------------------------------------------

//    public void insertImg(int id , Bitmap img )
//    {
//        byte[] data = getBitmapAsByteArray(img); // this is a function
//
//        insertStatement_logo.bindLong(1, id);
//        insertStatement_logo.bindBlob(2, data);
//
//        insertStatement_logo.executeInsert();
//        insertStatement_logo.clearBindings() ;
//
//    }
//
//    public static byte[] getBitmapAsByteArray(Bitmap bitmap)
//    {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
//        return outputStream.toByteArray();
//    }



//    public Bitmap getImage(int i)
//    {
//
//        String qu = "select img  from table where feedid=" + i ;
//        Cursor cur = db.rawQuery(qu, null);
//
//        if (cur.moveToFirst()){
//            byte[] imgByte = cur.getBlob(0);
//            cur.close();
//            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
//        }
//        if (cur != null && !cur.isClosed()) {
//            cur.close();
//        }
//
//        return null;
//    }

//--------------------------------------------------------------------------------------------------
//    kep letoltese egy url-rol

//    byte[] logoImage = getLogoImage(IMAGEURL);
//
//    private byte[] getLogoImage(String url){
//        try {
//            URL imageUrl = new URL(url);
//            URLConnection ucon = imageUrl.openConnection();
//
//            InputStream is = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//
//            ByteArrayBuffer baf = new ByteArrayBuffer(500);
//            int current = 0;
//            while ((current = bis.read()) != -1) {
//                baf.append((byte) current);
//            }
//
//            return baf.toByteArray();
//        } catch (Exception e) {
//            Log.d("ImageManager", "Error: " + e.toString());
//        }
//        return null;
//    }



    //kep elmentese

//    public void insertUser(){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        String delSql = "DELETE FROM ACCOUNTS";
//        SQLiteStatement delStmt = db.compileStatement(delSql);
//        delStmt.execute();
//
//        String sql = "INSERT INTO ACCOUNTS (account_id,account_name,account_image) VALUES(?,?,?)";
//        SQLiteStatement insertStmt = db.compileStatement(sql);
//        insertStmt.clearBindings();
//        insertStmt.bindString(1, Integer.toString(this.accId));
//        insertStmt.bindString(2,this.accName);
//        insertStmt.bindBlob(3, this.accImage);
//        insertStmt.executeInsert();
//        db.close();
//    }


    //kep visszakerese

//    public Account getCurrentAccount() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        String sql = "SELECT * FROM ACCOUNTS";
//        Cursor cursor = db.rawQuery(sql, new String[] {});
//
//        if(cursor.moveToFirst()){
//            this.accId  = cursor.getInt(0);
//            this.accName = cursor.getString(1);
//            this.accImage = cursor.getBlob(2);
//        }
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        db.close();
//        if(cursor.getCount() == 0){
//            return null;
//        } else {
//            return this;
//        }
//    }


    //betolteni egy imageview-ba

//    logoImage.setImageBitmap(BitmapFactory.decodeByteArray( currentAccount.accImage,
//        0,currentAccount.accImage.length));









//--------------------------------------------------------------------------------------------------
}
