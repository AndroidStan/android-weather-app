package com.weather.android.util.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CityDetailsDao {
    @Query("SELECT * FROM CityDetails LIMIT :nRecords")
    List<CityDetails> getFirstN(int nRecords);

    @Query("SELECT * FROM CityDetails LIMIT :startLimit, :endLimit")
    List<CityDetails> getRange(int startLimit, int endLimit);
}
