package com.example.mypage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WatchDto.class}, version = 2, exportSchema = false)
public abstract class WatchDB extends RoomDatabase {
    private static WatchDB database;
    private static String DATABASE_NAME = "database";

    public abstract WatchDao watchDao();

    public synchronized static WatchDB getInstance(Context context) {
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), WatchDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

}
