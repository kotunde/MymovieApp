package com.example.mymovieapp.Helpers.DBAdapters;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapterUsers extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "MovieAppUsers.db";
    public static final String USERS_TABLE = "Users";


    public DBAdapterUsers(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE  Users (id integer primary key, username VARCHAR, password VARCHAR,picture BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public boolean insertUser(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        contentValues.put("picture",new byte[0]);
        db.insert("Users", null, contentValues);
        db.close();
        return true;
    }

    public boolean changePassword(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",password);

        if(db.update("Users", contentValues, "username="+"'"+username+"'", null) > 0)
        {
            //db.close();
            return true;
        }
        else
        {
            //db.close();
            return false;
        }


    }

    public Cursor getDataUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users",null);
        //db.close();
        return res;
    }

    public Cursor getDataUsersById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users where id ="+ id + "",null );
        //db.close();
        return res;
    }

    public Cursor getPasswordByUsername(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users where username=" + "'" + username + "'",null );
        //db.close();
        return res;
    }

    //TODO
    public  boolean updateUserProfilePicture(String pUsername,byte[] profilePicture)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("picture",profilePicture);
        db.update("Users",contentValues,"username = ?",new String[] {pUsername});

        db.close();
        return true;
    }

    //TODO
    public Cursor getDataUsersByName(String pUsername)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users where username ="+"'" +pUsername + "'",null );
        return res;
    }
}
