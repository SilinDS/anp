package sds.auto.plate.db.tables;

/**
 * Created by sds on 04.03.16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import sds.auto.plate.base.EaistoTab;
import sds.auto.plate.db.SqliteHelper;


/**
 * @author Artur Vasilov
 */
public class EaistoTable {

  //  public static final Uri URI = SqliteHelper.BASE_CONTENT_URI.buildUpon().appendPath(Requests.TABLE_NAME).build();

    static String TABLE_NAME = EaistoTable.class.getSimpleName();
    private static SQLiteDatabase mDataBase;
    private static SqliteHelper mSqliteHelper;

    public EaistoTable(Context context) {
        mSqliteHelper = new SqliteHelper( context );
        mDataBase = mSqliteHelper.getWritableDatabase();
    }

    public static long add (@NonNull EaistoTab eaisto ) {
        long i = mDataBase.insert(TABLE_NAME, null, toContentValues( eaisto ));
        return i;
    }

    public interface Columns {
        String IDEAISTO = "id_eaisto";
        String IDPLATE = "id_plate";
        String TIMEDATE = "timedate";
        String DK = "dk";
        String CAPTION = "caption";
        String FRAME = "frame";
        String BODY = "body";
        String VIN = "vin";
        String PLATE = "plate";
        String STARTDATE = "startdate";
        String ENDDATE = "enddate";
        String OPERATOR = "operator";
        String EXPERT = "expert";
    }

    @NonNull
    public static ContentValues toContentValues(@NonNull EaistoTab eaisto) {
        ContentValues values = new ContentValues();
      //  values.put(Columns.IDEAISTO, eaisto.getIdEaisto());
        values.put(Columns.IDPLATE, eaisto.getIdPlate());
        values.put(Columns.TIMEDATE, eaisto.getTimedate());
        values.put(Columns.DK, eaisto.getDk());
        values.put(Columns.BODY, eaisto.getBody());
        values.put(Columns.FRAME, eaisto.getFrame());
        values.put(Columns.VIN, eaisto.getVin());
        values.put(Columns.CAPTION, eaisto.getCaption());
        values.put(Columns.PLATE, eaisto.getPlate());
        values.put(Columns.STARTDATE, eaisto.getStartdate());
        values.put(Columns.ENDDATE, eaisto.getEnddate());
        values.put(Columns.OPERATOR, eaisto.getOperator());
        values.put(Columns.EXPERT, eaisto.getExpert());
        return values;
    }

    @NonNull
    public static EaistoTab fromCursor(@NonNull Cursor cursor) {
        long id_eaisto = cursor.getLong(cursor.getColumnIndex(Columns.IDEAISTO));
        long id_plate = cursor.getLong(cursor.getColumnIndex(Columns.IDPLATE));
        long timedate = cursor.getLong(cursor.getColumnIndex(Columns.TIMEDATE));

        String dk = cursor.getString(cursor.getColumnIndex(Columns.DK));
        String body = cursor.getString (cursor.getColumnIndex(Columns.BODY));
        String frame = cursor.getString (cursor.getColumnIndex(Columns.FRAME));
        String vin = cursor.getString(cursor.getColumnIndex(Columns.VIN));
        String caption = cursor.getString(cursor.getColumnIndex(Columns.CAPTION));
        String plate = cursor.getString(cursor.getColumnIndex(Columns.PLATE));
        String startdate = cursor.getString(cursor.getColumnIndex(Columns.STARTDATE));
        String enddate = cursor.getString(cursor.getColumnIndex(Columns.ENDDATE));
        String operator = cursor.getString(cursor.getColumnIndex(Columns.OPERATOR));
        String expert = cursor.getString(cursor.getColumnIndex(Columns.EXPERT));
        return new EaistoTab ( id_eaisto, id_plate, timedate, dk, caption, vin,  body, frame,
                plate, startdate, enddate, operator, expert );
    }


    public interface Requests {

        String TABLE_NAME = EaistoTable.class.getSimpleName();
        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +

                Columns.IDEAISTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.IDPLATE + " LONG, " +
                Columns.TIMEDATE + " LONG, " +
                Columns.DK + " VARCHAR(20), " +
                Columns.BODY + " VARCHAR(20), " +
                Columns.FRAME + " VARCHAR(20), " +
                Columns.VIN + " VARCHAR(17), " +
                Columns.CAPTION + " VARCHAR(30), " +
                Columns.PLATE + " VARCHAR(9), " +
                Columns.STARTDATE	+ " VARCHAR(12), "	+
                Columns.ENDDATE	+ " VARCHAR(12), "	+
                Columns.OPERATOR	+ " TEXT, "	+

// последняя строка - без запятой!
                Columns.EXPERT + " VARCHAR(20) " +
                ");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}