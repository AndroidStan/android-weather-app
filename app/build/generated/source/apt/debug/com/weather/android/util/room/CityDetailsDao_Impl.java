package com.weather.android.util.room;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CityDetailsDao_Impl implements CityDetailsDao {
  private final RoomDatabase __db;

  public CityDetailsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public List<CityDetails> getFirstN(int nRecords) {
    final String _sql = "SELECT * FROM CityDetails LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, nRecords);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfCountry = _cursor.getColumnIndexOrThrow("country");
      final int _cursorIndexOfLongtitude = _cursor.getColumnIndexOrThrow("longtitude");
      final int _cursorIndexOfLatitude = _cursor.getColumnIndexOrThrow("latitude");
      final int _cursorIndexOfZipcode = _cursor.getColumnIndexOrThrow("zipcode");
      final int _cursorIndexOfStatename = _cursor.getColumnIndexOrThrow("statename");
      final List<CityDetails> _result = new ArrayList<CityDetails>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CityDetails _item;
        _item = new CityDetails();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpCountry;
        _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        _item.setCountry(_tmpCountry);
        final Double _tmpLongtitude;
        if (_cursor.isNull(_cursorIndexOfLongtitude)) {
          _tmpLongtitude = null;
        } else {
          _tmpLongtitude = _cursor.getDouble(_cursorIndexOfLongtitude);
        }
        _item.setLongtitude(_tmpLongtitude);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final Integer _tmpZipcode;
        if (_cursor.isNull(_cursorIndexOfZipcode)) {
          _tmpZipcode = null;
        } else {
          _tmpZipcode = _cursor.getInt(_cursorIndexOfZipcode);
        }
        _item.setZipcode(_tmpZipcode);
        final String _tmpStatename;
        _tmpStatename = _cursor.getString(_cursorIndexOfStatename);
        _item.setStatename(_tmpStatename);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CityDetails> getRange(int startLimit, int endLimit) {
    final String _sql = "SELECT * FROM CityDetails LIMIT ?, ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startLimit);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endLimit);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfCountry = _cursor.getColumnIndexOrThrow("country");
      final int _cursorIndexOfLongtitude = _cursor.getColumnIndexOrThrow("longtitude");
      final int _cursorIndexOfLatitude = _cursor.getColumnIndexOrThrow("latitude");
      final int _cursorIndexOfZipcode = _cursor.getColumnIndexOrThrow("zipcode");
      final int _cursorIndexOfStatename = _cursor.getColumnIndexOrThrow("statename");
      final List<CityDetails> _result = new ArrayList<CityDetails>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CityDetails _item;
        _item = new CityDetails();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpCountry;
        _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        _item.setCountry(_tmpCountry);
        final Double _tmpLongtitude;
        if (_cursor.isNull(_cursorIndexOfLongtitude)) {
          _tmpLongtitude = null;
        } else {
          _tmpLongtitude = _cursor.getDouble(_cursorIndexOfLongtitude);
        }
        _item.setLongtitude(_tmpLongtitude);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final Integer _tmpZipcode;
        if (_cursor.isNull(_cursorIndexOfZipcode)) {
          _tmpZipcode = null;
        } else {
          _tmpZipcode = _cursor.getInt(_cursorIndexOfZipcode);
        }
        _item.setZipcode(_tmpZipcode);
        final String _tmpStatename;
        _tmpStatename = _cursor.getString(_cursorIndexOfStatename);
        _item.setStatename(_tmpStatename);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
