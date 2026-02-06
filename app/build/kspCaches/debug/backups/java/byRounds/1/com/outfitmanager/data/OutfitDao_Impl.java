package com.outfitmanager.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.outfitmanager.domain.OutfitState;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OutfitDao_Impl implements OutfitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OutfitEntity> __insertionAdapterOfOutfitEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<OutfitEntity> __deletionAdapterOfOutfitEntity;

  private final EntityDeletionOrUpdateAdapter<OutfitEntity> __updateAdapterOfOutfitEntity;

  public OutfitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOutfitEntity = new EntityInsertionAdapter<OutfitEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `outfits` (`id`,`imageUri`,`name`,`type`,`category`,`state`,`wornSinceTimestamp`,`notes`,`lastUpdated`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OutfitEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getImageUri());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getCategory());
        final String _tmp = __converters.fromOutfitState(entity.getState());
        statement.bindString(6, _tmp);
        if (entity.getWornSinceTimestamp() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getWornSinceTimestamp());
        }
        statement.bindString(8, entity.getNotes());
        statement.bindLong(9, entity.getLastUpdated());
      }
    };
    this.__deletionAdapterOfOutfitEntity = new EntityDeletionOrUpdateAdapter<OutfitEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `outfits` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OutfitEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfOutfitEntity = new EntityDeletionOrUpdateAdapter<OutfitEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `outfits` SET `id` = ?,`imageUri` = ?,`name` = ?,`type` = ?,`category` = ?,`state` = ?,`wornSinceTimestamp` = ?,`notes` = ?,`lastUpdated` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OutfitEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getImageUri());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getCategory());
        final String _tmp = __converters.fromOutfitState(entity.getState());
        statement.bindString(6, _tmp);
        if (entity.getWornSinceTimestamp() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getWornSinceTimestamp());
        }
        statement.bindString(8, entity.getNotes());
        statement.bindLong(9, entity.getLastUpdated());
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insertOutfit(final OutfitEntity outfit,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfOutfitEntity.insertAndReturnId(outfit);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOutfit(final OutfitEntity outfit,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfOutfitEntity.handle(outfit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOutfit(final OutfitEntity outfit,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfOutfitEntity.handle(outfit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<OutfitEntity>> getAllOutfits() {
    final String _sql = "SELECT * FROM outfits ORDER BY lastUpdated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"outfits"}, new Callable<List<OutfitEntity>>() {
      @Override
      @NonNull
      public List<OutfitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfWornSinceTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "wornSinceTimestamp");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<OutfitEntity> _result = new ArrayList<OutfitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OutfitEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final OutfitState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toOutfitState(_tmp);
            final Long _tmpWornSinceTimestamp;
            if (_cursor.isNull(_cursorIndexOfWornSinceTimestamp)) {
              _tmpWornSinceTimestamp = null;
            } else {
              _tmpWornSinceTimestamp = _cursor.getLong(_cursorIndexOfWornSinceTimestamp);
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new OutfitEntity(_tmpId,_tmpImageUri,_tmpName,_tmpType,_tmpCategory,_tmpState,_tmpWornSinceTimestamp,_tmpNotes,_tmpLastUpdated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<OutfitEntity>> getOutfitsByState(final OutfitState state) {
    final String _sql = "SELECT * FROM outfits WHERE state = ? ORDER BY lastUpdated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromOutfitState(state);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"outfits"}, new Callable<List<OutfitEntity>>() {
      @Override
      @NonNull
      public List<OutfitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfWornSinceTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "wornSinceTimestamp");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<OutfitEntity> _result = new ArrayList<OutfitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OutfitEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final OutfitState _tmpState;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toOutfitState(_tmp_1);
            final Long _tmpWornSinceTimestamp;
            if (_cursor.isNull(_cursorIndexOfWornSinceTimestamp)) {
              _tmpWornSinceTimestamp = null;
            } else {
              _tmpWornSinceTimestamp = _cursor.getLong(_cursorIndexOfWornSinceTimestamp);
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new OutfitEntity(_tmpId,_tmpImageUri,_tmpName,_tmpType,_tmpCategory,_tmpState,_tmpWornSinceTimestamp,_tmpNotes,_tmpLastUpdated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getOutfitById(final int id, final Continuation<? super OutfitEntity> $completion) {
    final String _sql = "SELECT * FROM outfits WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OutfitEntity>() {
      @Override
      @Nullable
      public OutfitEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfWornSinceTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "wornSinceTimestamp");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final OutfitEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final OutfitState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toOutfitState(_tmp);
            final Long _tmpWornSinceTimestamp;
            if (_cursor.isNull(_cursorIndexOfWornSinceTimestamp)) {
              _tmpWornSinceTimestamp = null;
            } else {
              _tmpWornSinceTimestamp = _cursor.getLong(_cursorIndexOfWornSinceTimestamp);
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new OutfitEntity(_tmpId,_tmpImageUri,_tmpName,_tmpType,_tmpCategory,_tmpState,_tmpWornSinceTimestamp,_tmpNotes,_tmpLastUpdated);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getWornOutfits(final Continuation<? super List<OutfitEntity>> $completion) {
    final String _sql = "SELECT * FROM outfits WHERE state = 'WORN' AND wornSinceTimestamp IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OutfitEntity>>() {
      @Override
      @NonNull
      public List<OutfitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfWornSinceTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "wornSinceTimestamp");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<OutfitEntity> _result = new ArrayList<OutfitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OutfitEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final OutfitState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.toOutfitState(_tmp);
            final Long _tmpWornSinceTimestamp;
            if (_cursor.isNull(_cursorIndexOfWornSinceTimestamp)) {
              _tmpWornSinceTimestamp = null;
            } else {
              _tmpWornSinceTimestamp = _cursor.getLong(_cursorIndexOfWornSinceTimestamp);
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new OutfitEntity(_tmpId,_tmpImageUri,_tmpName,_tmpType,_tmpCategory,_tmpState,_tmpWornSinceTimestamp,_tmpNotes,_tmpLastUpdated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getNeedsLaundryCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM outfits WHERE state = 'NEEDS_LAUNDRY'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
