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

import sds.auto.plate.base.EaistoTab;
import sds.auto.plate.base.GibddTab;
import sds.auto.plate.base.PlateTab;
import sds.auto.plate.db.SqliteHelper;


/**
 * @author Artur Vasilov
 */
public class GibddTable {

  //  public static final Uri URI = SqliteHelper.BASE_CONTENT_URI.buildUpon().appendPath(Requests.TABLE_NAME).build();

    static String TABLE_NAME = GibddTable.class.getSimpleName();
    private static SQLiteDatabase mDataBase;
    private static SqliteHelper mSqliteHelper;

    public GibddTable(Context context) {
        mSqliteHelper = new SqliteHelper( context );
        mDataBase = mSqliteHelper.getWritableDatabase();
    }

    public static long add (@NonNull GibddTab gibdd ) {
        long i = mDataBase.insert(TABLE_NAME, null, toContentValues( gibdd ));
        return i;
    }

    public int update ( GibddTab gibdd ) {
        ContentValues cv = toContentValues( gibdd );
        int numColumns =  mDataBase.update(TABLE_NAME, cv, Columns.IDPLATE + " = ?",
                new String[]{ String.valueOf( gibdd.getIdPlate() ) });
        return numColumns;
    }

    public interface Columns {
        String ID = "id_";
        String IDPLATE = "id_plate";
        String TDREGISTER = "d_register";
        String TDACCIDENTS = "d_accidents";
        String TDWANTED = "d_wanted";
        String TDRESTRICT = "d_restrict";
        String CATEGORY = "category";
        String COLOR = "color";
        String ENGINENUMBER = "engine_number";
        String ENGINEVOLUME = "engine_volume";
        String MODEL = "model";
        String POWER = "power";
        String TYPE = "type";
        String YEAR = "year";
        String OWNERSHIP = "ownership";
        String ACCIDENTS = "accidents";
        String WANTED = "wanted";
        String RESTRICT = "restrict";
    }


    @NonNull
    public static ContentValues toContentValues(@NonNull GibddTab gibdd) {
        ContentValues values = new ContentValues();
      //  values.put(Columns.ID, gibdd.getId());
        values.put(Columns.IDPLATE, gibdd.getIdPlate());
        values.put(Columns.TDREGISTER, gibdd.getDateRegister());
        values.put(Columns.TDACCIDENTS, gibdd.getDateAccidents());
        values.put(Columns.TDWANTED, gibdd.getDateWanted());
        values.put(Columns.TDRESTRICT, gibdd.getDateRestrict());
        values.put(Columns.CATEGORY, gibdd.getCategory());
        values.put(Columns.COLOR, gibdd.getColor());
        values.put(Columns.ENGINENUMBER, gibdd.getEngineNumber());
        values.put(Columns.ENGINEVOLUME, gibdd.getEngineVolume());
        values.put(Columns.MODEL, gibdd.getModel());
        values.put(Columns.POWER, gibdd.getPower());
        values.put(Columns.TYPE, gibdd.getType());
        values.put(Columns.YEAR, gibdd.getYear());
        values.put(Columns.OWNERSHIP, gibdd.getOwnership());
        values.put(Columns.ACCIDENTS, gibdd.getAccidents());
        values.put(Columns.WANTED, gibdd.getWanted());
        values.put(Columns.RESTRICT, gibdd.getRestrict());

        return values;
    }

    @NonNull
    public static GibddTab fromCursor(@NonNull Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Columns.ID));
        long idPlate = cursor.getLong(cursor.getColumnIndex(Columns.IDPLATE));
        long tdRegister = cursor.getLong(cursor.getColumnIndex(Columns.TDREGISTER));
        long tdAccidents = cursor.getLong(cursor.getColumnIndex(Columns.TDACCIDENTS));
        long tdWanted = cursor.getLong(cursor.getColumnIndex(Columns.TDWANTED));
        long tdRestrict = cursor.getLong(cursor.getColumnIndex(Columns.TDRESTRICT));
        String category = cursor.getString(cursor.getColumnIndex(Columns.CATEGORY));
        String color = cursor.getString (cursor.getColumnIndex(Columns.COLOR));
        String enumber = cursor.getString (cursor.getColumnIndex(Columns.ENGINENUMBER));
        String evolume = cursor.getString(cursor.getColumnIndex(Columns.ENGINEVOLUME));
        String model = cursor.getString(cursor.getColumnIndex(Columns.MODEL));
        String power = cursor.getString(cursor.getColumnIndex(Columns.POWER));
        String type = cursor.getString(cursor.getColumnIndex(Columns.TYPE));
        int year = cursor.getInt(cursor.getColumnIndex(Columns.YEAR));
        String ownwr = cursor.getString(cursor.getColumnIndex(Columns.OWNERSHIP));
        String accidents = cursor.getString(cursor.getColumnIndex(Columns.ACCIDENTS));
        String wanted = cursor.getString(cursor.getColumnIndex(Columns.WANTED));
        String restrict = cursor.getString(cursor.getColumnIndex(Columns.RESTRICT));
        return new GibddTab ( id, idPlate, tdRegister, tdAccidents, tdWanted, tdRestrict,
                category, color, enumber, evolume, model, power, type, year,
                ownwr, accidents, wanted, restrict);
    }

    public GibddTab get ( long n ) {
        Cursor  mCursor = mDataBase.query( TABLE_NAME, null, Columns.IDPLATE + " = ?",
                new String[] { String.valueOf( n ) }, null, null, null );

        if (!mCursor.moveToLast()) {
            return null;
        }
        try {
            mCursor.moveToFirst();
            return fromCursor( mCursor );
        }  finally {
            mCursor.close();
        }

    }

    public static List<GibddTab> getAll (int favorite ) {

        SQLiteDatabase database = mSqliteHelper.getWritableDatabase();

        Cursor mCursor = database.query( TABLE_NAME, null, null, null, null,
                null, null );
        List<GibddTab> gibddTabs = new ArrayList<>();

        if (!mCursor.moveToLast()) {
            return gibddTabs;
        }
        try {
            do {
                switch ( favorite ) {
                    case 0:
                        gibddTabs.add(fromCursor(mCursor)); // все нужны
                        break;
                    case 1:
                        gibddTabs.add(fromCursor(mCursor)); // только избранные
                        break;
                }
            } while (mCursor.moveToPrevious());
            return gibddTabs;
        } finally {
            mCursor.close();
        }
    }

    public interface Requests {

        String TABLE_NAME = GibddTable.class.getSimpleName();
        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +

                Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.IDPLATE + " LONG, " +
                Columns.TDREGISTER + " LONG, " +
                Columns.TDACCIDENTS + " LONG, " +
                Columns.TDWANTED + " LONG, " +
                Columns.TDRESTRICT + " LONG, " +
                Columns.CATEGORY + " VARCHAR(2), " +
                Columns.COLOR + " VARCHAR(20), " +
                Columns.ENGINENUMBER + " VARCHAR(20), " +
                Columns.ENGINEVOLUME + " VARCHAR(10), " +
                Columns.MODEL + " VARCHAR(30), " +
                Columns.POWER + " VARCHAR(20), " +
                Columns.TYPE	+ " VARCHAR(45), "	+
                Columns.YEAR	+ " INTEGER, "	+
                Columns.OWNERSHIP	+ " TEXT, "	+
                Columns.ACCIDENTS	+ " TEXT, "	+
                Columns.WANTED	+ " TEXT, "	+

// последняя строка - без запятой!
                Columns.RESTRICT + " TEXT " +
                ");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}