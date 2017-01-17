package sds.auto.plate.db.tables;

/**
 * Created by sds on 04.03.16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sds.auto.plate.base.PlateTab;
import sds.auto.plate.db.SqliteHelper;


/**
 * @author Artur Vasilov
 */
public class PlateTable {

  //  public static final Uri URI = SqliteHelper.BASE_CONTENT_URI.buildUpon().appendPath(Requests.TABLE_NAME).build();


    private static String TABLE_NAME = PlateTable.class.getSimpleName();
    private static SQLiteDatabase mDataBase;
    private static SqliteHelper mSqliteHelper;

    public PlateTable(Context context) {
        mSqliteHelper = new SqliteHelper( context );
        mDataBase = mSqliteHelper.getWritableDatabase();
    }

    public static long add ( @NonNull PlateTab plateTab) {
        long i = mDataBase.insert(TABLE_NAME, null, toContentValues(plateTab));
        return i;
    }

    public static long delete (long id) {
        return mDataBase.delete(TABLE_NAME, Columns.IDPLATE + " = ?",
                new String[] { String.valueOf(id) });

    }

    private interface Columns {
        String ID = "id_";
        String IDPLATE = "id_plate";
        String NUMBER = "number";
        String ISVIN = "isvin";
        String VIN = "vin";
        String TIMEDATE = "timedate";
        String CAPTION = "caption";
        String YEAR = "year";
        String COLOR = "color";
        String STATUS = "status";
        String FAVORITE = "favorite";
        String NOTE = "note";
        String EQUIPMENT = "equip";
    }

    @NonNull
    private static ContentValues toContentValues(@NonNull PlateTab plateTab) {
        ContentValues values = new ContentValues();
      //  values.put(Columns.ID, plateTab.id());
        values.put(Columns.IDPLATE, plateTab.getIdPlate());
        values.put(Columns.NUMBER, plateTab.getNumber());
        values.put(Columns.ISVIN, plateTab.getIsVin());
        values.put(Columns.VIN, plateTab.getVin());
        values.put(Columns.TIMEDATE, plateTab.getTimedate());
        values.put(Columns.CAPTION, plateTab.getCaption());
        values.put(Columns.YEAR, plateTab.getYear());
        values.put(Columns.COLOR, plateTab.getColor());
        values.put(Columns.STATUS, plateTab.getStatus());
        values.put(Columns.FAVORITE, plateTab.getFavorite());
        values.put(Columns.NOTE, plateTab.getNote());
        values.put(Columns.EQUIPMENT, plateTab.getEquipment());
        return values;
    }

    @NonNull
    private static PlateTab fromCursor(@NonNull Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Columns.ID));
        long id_plate = cursor.getLong(cursor.getColumnIndex(Columns.IDPLATE));
        String number = cursor.getString(cursor.getColumnIndex(Columns.NUMBER));
        String vin = cursor.getString(cursor.getColumnIndex(Columns.VIN));
        int isvin = cursor.getInt (cursor.getColumnIndex(Columns.ISVIN));
        long timedate = cursor.getLong(cursor.getColumnIndex(Columns.TIMEDATE));
        String caption = cursor.getString(cursor.getColumnIndex(Columns.CAPTION));
        int year = cursor.getInt(cursor.getColumnIndex(Columns.YEAR));
        String color = cursor.getString(cursor.getColumnIndex(Columns.COLOR));
        int status = cursor.getInt(cursor.getColumnIndex(Columns.STATUS));
        int favorite = cursor.getInt(cursor.getColumnIndex(Columns.FAVORITE));
        String note = cursor.getString(cursor.getColumnIndex(Columns.NOTE));
        String equip = cursor.getString(cursor.getColumnIndex(Columns.EQUIPMENT));

        return new PlateTab (id, id_plate, number, isvin, vin, timedate, caption,
                year, color, status, favorite, note, equip );
    }

    public List<PlateTab> getAll (int favorite ) {

        SQLiteDatabase database = mSqliteHelper.getWritableDatabase();

        Cursor mCursor = database.query( Requests.TABLE_NAME, null, null, null, null,
                null, null );
        List<PlateTab> plateTabs = new ArrayList<>();

        if (!mCursor.moveToLast()) {
            return plateTabs;
        }
        try {
            do {
                switch ( favorite ) {
                    case 0:
                        plateTabs.add(fromCursor(mCursor)); // все нужны
                        break;
                    case 1:
                        plateTabs.add(fromCursor(mCursor)); // только избранные
                        break;
                }
            } while (mCursor.moveToPrevious());
            return plateTabs;
        } finally {
            mCursor.close();
        }
    }

    public int saveEquip(PlateTab autoItem) {
        ContentValues cv = new ContentValues();
        cv.put(Columns.EQUIPMENT, autoItem.getEquipment());
        return mDataBase.update(TABLE_NAME, cv, Columns.VIN + " = ?",
                new String[]{String.valueOf(autoItem.getVin())});
    }

    public int update (PlateTab autoItem) {
        ContentValues cv = toContentValues (autoItem);
        int numColumns =  mDataBase.update(TABLE_NAME, cv, PlateTable.Columns.IDPLATE + " = ?",
                new String[]{ String.valueOf( autoItem.getIdPlate() ) });
        return numColumns;

    }

    public PlateTab get ( long n ) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, Columns.ID + " = ?",
                new String[] { String.valueOf(n) }, null, null, Columns.TIMEDATE);

        mCursor.moveToFirst();

        return fromCursor(mCursor);

    }

    public interface Requests {

        String TABLE_NAME = PlateTable.class.getSimpleName();
        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +

                Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.IDPLATE + " LONG, " +
                Columns.NUMBER + " VARCHAR(9), " +
                Columns.ISVIN + " INTEGER, " +
                Columns.VIN + " VARCHAR(17), " +
                Columns.TIMEDATE + " LONG, " +
                Columns.CAPTION + " VARCHAR(30), " +
                Columns.YEAR + " INTEGER, " +
                Columns.COLOR + " VARCHAR(30), " +
                Columns.STATUS	+ " INTEGER, "	+
                Columns.FAVORITE + " INTEGER, " +
                Columns.NOTE + " TEXT, " +
                // последняя строка - без запятой!
                Columns.EQUIPMENT + " TEXT " +
                ");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}