package sds.auto.plate.db;

/**
 * Created by sds on 04.03.16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sds.auto.plate.db.tables.EaistoTable;
import sds.auto.plate.db.tables.GibddTable;
import sds.auto.plate.db.tables.PlateTable;
import sds.auto.plate.utility.LogUtil;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sds.auto.db";
    private static final int DATABASE_VERSION = 1;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlateTable.Requests.CREATION_REQUEST);
        LogUtil.logE("MySQL","create PlateTable" + PlateTable.Requests.CREATION_REQUEST );

        db.execSQL(EaistoTable.Requests.CREATION_REQUEST);
        LogUtil.logE("MySQL","create EaistoTable" + EaistoTable.Requests.CREATION_REQUEST );

        db.execSQL(GibddTable.Requests.CREATION_REQUEST);
        LogUtil.logE("MySQL","create GibddTable" + GibddTable.Requests.CREATION_REQUEST );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PlateTable.Requests.DROP_REQUEST);
        LogUtil.logE("MySQL","Upgrade PlateTable"  + PlateTable.Requests.DROP_REQUEST );
        db.execSQL(EaistoTable.Requests.DROP_REQUEST);
        LogUtil.logE("MySQL","Upgrade EaistoTable"  + EaistoTable.Requests.DROP_REQUEST );
        db.execSQL(EaistoTable.Requests.DROP_REQUEST);
        LogUtil.logE("MySQL","Upgrade GibddTable"  + GibddTable.Requests.DROP_REQUEST );
        onCreate(db);
    }
}