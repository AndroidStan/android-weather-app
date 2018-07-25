package com.weather.android.util.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class USACitiesDatabase_Impl extends USACitiesDatabase {
  private volatile CityDetailsDao _cityDetailsDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `CityDetails` (`id` INTEGER, `name` TEXT, `country` TEXT, `longtitude` REAL, `latitude` REAL, `zipcode` INTEGER, `statename` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"16d13febe5de98e58a3a3612a6485448\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `CityDetails`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCityDetails = new HashMap<String, TableInfo.Column>(7);
        _columnsCityDetails.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsCityDetails.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsCityDetails.put("country", new TableInfo.Column("country", "TEXT", false, 0));
        _columnsCityDetails.put("longtitude", new TableInfo.Column("longtitude", "REAL", false, 0));
        _columnsCityDetails.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0));
        _columnsCityDetails.put("zipcode", new TableInfo.Column("zipcode", "INTEGER", false, 0));
        _columnsCityDetails.put("statename", new TableInfo.Column("statename", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCityDetails = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCityDetails = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCityDetails = new TableInfo("CityDetails", _columnsCityDetails, _foreignKeysCityDetails, _indicesCityDetails);
        final TableInfo _existingCityDetails = TableInfo.read(_db, "CityDetails");
        if (! _infoCityDetails.equals(_existingCityDetails)) {
          throw new IllegalStateException("Migration didn't properly handle CityDetails(com.weather.android.util.room.CityDetails).\n"
                  + " Expected:\n" + _infoCityDetails + "\n"
                  + " Found:\n" + _existingCityDetails);
        }
      }
    }, "16d13febe5de98e58a3a3612a6485448", "4d824690d087e2cc949c18798f85c828");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "CityDetails");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `CityDetails`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public CityDetailsDao cityDetailsDao() {
    if (_cityDetailsDao != null) {
      return _cityDetailsDao;
    } else {
      synchronized(this) {
        if(_cityDetailsDao == null) {
          _cityDetailsDao = new CityDetailsDao_Impl(this);
        }
        return _cityDetailsDao;
      }
    }
  }
}
