package com.weather.android.util.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {CityDetails.class}, version = 2)
public abstract class USACitiesDatabase extends RoomDatabase {
    public abstract CityDetailsDao cityDetailsDao();
}
